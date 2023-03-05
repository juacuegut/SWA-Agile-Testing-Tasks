package edu.upc.talent.swqa.campus.infrastructure;

import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import edu.upc.talent.swqa.jdbc.Jdbc;

import java.sql.SQLException;
import java.util.List;

import static edu.upc.talent.swqa.jdbc.Param.p;
import static edu.upc.talent.swqa.util.Utils.eq;

public class PostgreSqlUsersRepository implements UsersRepository {

  private final Jdbc db;

  public PostgreSqlUsersRepository(Jdbc db) {
    this.db = db;
  }

  @Override
  public void createUser(String name, String surname, String email, String role, String groupName) {
    try {
      db.update(
            "insert into users (name, surname, email, role, group_id) " +
                  "values (?, ?, ?, ?, (select id from groups where name = ?))",
            p(name), p(surname), p(email), p(role), p(groupName)
      );
    } catch (RuntimeException e) {
      var cause = e.getCause();
      if (cause instanceof SQLException &&
            eq(((SQLException) cause).getSQLState(), "23502") &&
            cause.getMessage().contains("null value in column \"group_id\""))
        throw new IllegalArgumentException("Group " + groupName + " does not exist");
      throw e;
    }
  }

  @Override
  public void createGroup(String name) {
    db.update("insert into groups (name) values (?)", p(name));
  }

  private final String selectUsersByGroupName = """
        select u.id, u.name, u.surname, u.email, u.role
        from users u join groups g on u.group_id = g.id
        where u.active and g.name = ?""";

  @Override
  public List<User> getUsersByGroupAndRole(String groupName, String onlyRole) {
    return db.select(
          selectUsersByGroupName + " and u.role = ?",
          (rs) -> new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), groupName),
          p(groupName), p(onlyRole)
    );
  }

  @Override
  public List<User> getUsersByGroup(String groupName) {
    return db.select(
          selectUsersByGroupName,
          (rs) -> new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), groupName),
          p(groupName)
    );
  }
}
