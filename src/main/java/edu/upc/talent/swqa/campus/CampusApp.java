package edu.upc.talent.swqa.campus;

import edu.upc.talent.swqa.jdbc.Jdbc;

import static edu.upc.talent.swqa.jdbc.Param.p;

public final class CampusApp {
  public CampusApp(Jdbc db) {
    this.db = db;
  }

  private final Jdbc db;

  private void sendEmail(String email, String subject, String body) {
    System.out.println("To: " + email + "\nSubject: " + subject + "\nBody:\n" + body + "\n");
  }

  public void createGroup(String name) {
    db.update("insert into groups (name) values (?)", p(name));
  }

  public void createUser(String name, String surname, String email, String role, String groupName) {
    db.update(
          "insert into users (name, surname, email, role, group_id) " +
                "values (?, ?, ?, ?, (select id from groups where name = ?))",
          p(name), p(surname), p(email), p(role), p(groupName)
    );
  }

  public void sendMailToGroupRole(String groupName, String onlyRole, String subject, String body) {
    var users = db.select(
          selectUsersByGroupName + " and u.role = ?",
          (rs) -> new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)),
          p(groupName), p(onlyRole)
    );
    users.forEach(u -> sendEmail(u.email(), subject, body));
  }

  private String selectUsersByGroupName = """
        select u.id, u.name, u.surname, u.email, u.role
        from users u join groups g on u.group_id = g.id
        where u.active and g.name = ?""";

  public void sendMailToGroup(String groupName, String subject, String body) {
    var users = db.select(
          selectUsersByGroupName,
          (rs) -> new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)),
          p(groupName)
    );
    users.forEach(u -> sendEmail(u.email(), subject, body));
  }
}
