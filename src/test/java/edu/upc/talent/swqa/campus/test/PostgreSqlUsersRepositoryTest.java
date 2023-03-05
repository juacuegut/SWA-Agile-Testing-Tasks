package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.PostgreSqlUsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.UsersDb;
import edu.upc.talent.swqa.jdbc.Jdbc;
import edu.upc.talent.swqa.jdbc.test.JdbcTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.function.BiConsumer;

import static edu.upc.talent.swqa.jdbc.test.Utils.*;
import static edu.upc.talent.swqa.util.Utils.join;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostgresQlUsersRepositoryStateGetter implements UsersRepositoryStateGetter {

  private final Jdbc db;

  public PostgresQlUsersRepositoryStateGetter(Jdbc db) {
    this.db = db;
  }

  public UsersRepositoryState get() {
    return new UsersRepositoryState(
          new HashSet<>(db.select(
                """
                      select u.id, u.name, u.surname, u.email, u.role, g.name 
                      from users u join groups g on u.group_id = g.id""",
                (rs) -> new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6))
          )),
          new HashSet<>(db.select(
                "SELECT id, name FROM groups",
                (rs) -> new Group(rs.getInt(1), rs.getString(2))
          ))
    );
  }

}

public class PostgreSqlUsersRepositoryTest {

  private static String templateDatabaseName = JdbcTest.class.getSimpleName().toLowerCase();

  @BeforeAll
  public static void setup() {
    initTemplateDatabase(templateDatabaseName, (db) -> {
            db.update(UsersDb.groupsTableDml);
            db.update(UsersDb.usersTableDml);
          }
    );
  }

  private List<User> initialUsers = List.of(
        new User("1", "John", "Doe", "john.doe@example.com", "student", "swqa"),
        new User("2", "Jane", "Doe", "jane.doe@example.com", "student", "swqa"),
        new User("3", "Mariah", "Hairam", "mariah.hairam@example.com", "teacher", "swqa")
  );

  private void test(String testName, BiConsumer<UsersRepository, UsersRepositoryStateGetter> test) {
    withTestDatabase(templateDatabaseName, testName, (db) -> {
//      var state = new UsersRepositoryState();
//      UsersRepositoryStateGetter stateGetter = () -> state;
//      var repository = new InMemoryUsersRepository(state);
      var stateGetter = new PostgresQlUsersRepositoryStateGetter(db);
      var repository = new PostgreSqlUsersRepository(db);
      repository.createGroup("swqa");
      initialUsers.forEach((user) ->
            repository.createUser(user.name(), user.surname(), user.email(), user.role(), user.groupName())
      );
      try {
        test.accept(repository, stateGetter);
      } catch (Exception e) {
        System.out.println("Test failed! Here you have the DB contents for debugging:");
        printDbContents(db);
        throw e;
      }
    });
  }

  @Test
  public void testGetUsersByGroup() {
    test("testGetUsersByGroup", (repository, stateGetter) -> {
      var actual = repository.getUsersByGroup("swqa");
      assertEquals(new HashSet<>(initialUsers), new HashSet<>(actual));
    });
  }

  @Test
  public void testCreateUser() {
    var name = "Jack";
    var surname = "Doe";
    var email = "jack.doe@example.com";
    var role = "student";
    var groupName = "swqa";
    var expectedNewUser = new User("4", name, surname, email, role, groupName);
    var expected = new HashSet<>(join(initialUsers, List.of(expectedNewUser)));
    test("testCreateUser", (repository, stateGetter) -> {
      repository.createUser(name, surname, email, role, groupName);
      var actual = new HashSet<>(stateGetter.get().users());
      assertEquals(expected, actual);
    });
  }

  @Test
  public void testCreateUserFailsIfGroupDoesNotExist() {
    test("testCreateUser", (repository, stateGetter) -> {
      var groupName = "non-existent";
      var exception = assertThrows(RuntimeException.class, () ->
            repository.createUser("a", "b", "a.b@example.com", "student", groupName)
      );
      assertEquals("Group " + groupName + " does not exist", exception.getMessage());
    });
  }
}
