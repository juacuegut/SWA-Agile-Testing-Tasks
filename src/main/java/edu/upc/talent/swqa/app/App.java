package edu.upc.talent.swqa.app;

import edu.upc.talent.swqa.jdbc.Jdbc;

import javax.sql.DataSource;

import static edu.upc.talent.swqa.functions.Utils.eq;
import static edu.upc.talent.swqa.jdbc.Param.p;

public final class App {
  public App(DataSource dataSource) {
    this.jdbc = new Jdbc(dataSource);
  }

  private final Jdbc jdbc;

  public void setEmail(String email, String subject, String body) {
    System.out.println("To " + email + "\nSubject: " + subject + "\nBody:\n" + body + "\n");
  }

  public void createUser(String id, String name, String surname, String email, String role, String groupName) {
    jdbc.inTransaction((tx) ->
          tx.update(
                "insert into users (id, name, surname, email, role, group_id) " +
                      "values (?, ?, ?, ?, ?, (select id from groups where name = ?))",
                p(id), p(name), p(surname), p(email), p(role), p(groupName)
          )
    );
  }

  public void sendMailToTeachers(String groupName, String subject, String body) {
    var users =
          jdbc.inTransaction((tx) ->
                tx.select(
                      "select u.id, u.name, u.surname, u.email, u.role " +
                            "from users u join groups g on u.group_id = g.id " +
                            "where active = true amd g.name = ?",
                      (rs) -> new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)),
                      p(groupName)
                )
          );
    users.forEach(u -> {
      if (eq(u.role(), "teacher")) setEmail(u.email(), subject, body);
    });

  }
}
