package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.PostgreSqlUsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.UsersDb;
import edu.upc.talent.swqa.campus.test.utils.InMemoryUsersRepository;
import edu.upc.talent.swqa.campus.test.utils.PostgreSqlUsersRepositoryStateGetter;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryStateGetter;
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

public interface UsersRepositoryTest {
  void test(String testName, BiConsumer<UsersRepository, UsersRepositoryStateGetter> test);

  List<User> initialUsers = List.of(
        new User("1", "John", "Doe", "john.doe@example.com", "student", "swqa"),
        new User("2", "Jane", "Doe", "jane.doe@example.com", "student", "swqa"),
        new User("3", "Mariah", "Hairam", "mariah.hairam@example.com", "teacher", "swqa")
  );

  default void setUpInitialData(UsersRepository repository) {
    repository.createGroup("swqa");
    initialUsers.forEach((user) ->
          repository.createUser(user.name(), user.surname(), user.email(), user.role(), user.groupName())
    );
  }

  @Test
  default void testGetUsersByGroup() {
    test("testGetUsersByGroup", (repository, stateGetter) -> {
      var actual = repository.getUsersByGroup("swqa");
      assertEquals(new HashSet<>(initialUsers), new HashSet<>(actual));
    });
  }

  @Test
  default void testCreateUser() {
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
  default void testCreateUserFailsIfGroupDoesNotExist() {
    test("testCreateUser", (repository, stateGetter) -> {
      var groupName = "non-existent";
      var exception = assertThrows(RuntimeException.class, () ->
            repository.createUser("a", "b", "a.b@example.com", "student", groupName)
      );
      assertEquals("Group " + groupName + " does not exist", exception.getMessage());
    });
  }

}

class PostgreSqlUsersRepositoryTest implements UsersRepositoryTest {

  private static String templateDatabaseName = JdbcTest.class.getSimpleName().toLowerCase();

  @BeforeAll
  public static void setup() {
    initTemplateDatabase(templateDatabaseName, (db) -> {
            db.update(UsersDb.groupsTableDml);
            db.update(UsersDb.usersTableDml);
          }
    );
  }

  public void test(String testName, BiConsumer<UsersRepository, UsersRepositoryStateGetter> test) {
    withTestDatabase(templateDatabaseName, testName, (db) -> {
      var stateGetter = new PostgreSqlUsersRepositoryStateGetter(db);
      var repository = new PostgreSqlUsersRepository(db);
      setUpInitialData(repository);
      try {
        test.accept(repository, stateGetter);
      } catch (Exception e) {
        System.out.println("Test failed! Here you have the DB contents for debugging:");
        printDbContents(db);
        throw e;
      }
    });
  }

}

class InMemoryUsersRepositoryTest implements UsersRepositoryTest {

  @Override
  public void test(String testName, BiConsumer<UsersRepository, UsersRepositoryStateGetter> test) {
    var state = new UsersRepositoryState();
    UsersRepositoryStateGetter stateGetter = () -> state;
    var repository = new InMemoryUsersRepository(state);
    setUpInitialData(repository);
    test.accept(repository, stateGetter);
  }
}
