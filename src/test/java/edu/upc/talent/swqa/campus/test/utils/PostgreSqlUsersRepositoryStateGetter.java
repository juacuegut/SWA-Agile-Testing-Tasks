package edu.upc.talent.swqa.campus.test.utils;

import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.jdbc.Jdbc;

import java.util.HashSet;

public class PostgreSqlUsersRepositoryStateGetter implements UsersRepositoryStateGetter {

  private final Jdbc db;

  public PostgreSqlUsersRepositoryStateGetter(Jdbc db) {
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