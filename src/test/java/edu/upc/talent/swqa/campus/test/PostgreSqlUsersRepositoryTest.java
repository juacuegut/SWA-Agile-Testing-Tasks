package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.PostgreSqlUsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.UsersDb;
import edu.upc.talent.swqa.campus.test.utils.Group;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;
import edu.upc.talent.swqa.jdbc.Database;
import edu.upc.talent.swqa.test.utils.DatabaseTest;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Consumer;

class PostgreSqlUsersRepositoryTest extends DatabaseTest implements UsersRepositoryTest {

  @Override
  protected void setUpDatabaseSchema(Database db) {
    db.update(UsersDb.groupsTableDml);
    db.update(UsersDb.usersTableDml);
  }

  public UsersRepositoryState testAndGetFinalState(String testName, UsersRepositoryState initialState, Consumer<UsersRepository> test) {
    return databaseTest(testName, (db) -> {
      var repository = new PostgreSqlUsersRepository(db);
      initialState.groups().forEach((group) -> repository.createGroup(group.name()));
      initialState.users().stream().sorted(Comparator.comparing(User::id)).forEach((user) -> repository.createUser(user.name(), user.surname(), user.email(), user.role(), user.groupName()));
      test.accept(repository);
      return getUsersRepositoryState(db);
    });
  }

  @NotNull
  private static UsersRepositoryState getUsersRepositoryState(Database db) {
    return new UsersRepositoryState(
          db.selectToSet(
                """
                      select u.id, u.name, u.surname, u.email, u.role, g.name 
                      from users u join groups g on u.group_id = g.id""",
                (rs) -> new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6))
          ),
          db.selectToSet(
                "SELECT id, name FROM groups",
                (rs) -> new Group(rs.getInt(1), rs.getString(2))
          )
    );
  }

}