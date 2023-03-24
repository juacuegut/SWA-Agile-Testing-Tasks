package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.CampusApp;
import edu.upc.talent.swqa.campus.infrastructure.ConsoleEmailSender;
import edu.upc.talent.swqa.campus.infrastructure.PostgreSqlUsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.UsersDb;
import edu.upc.talent.swqa.test.utils.DatabaseBackedTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static edu.upc.talent.swqa.test.utils.Asserts.assertEquals;

public final class CampusAppEndToEndTest extends DatabaseBackedTest {

  private CampusApp app;

  @BeforeEach
  public void setUpDatabaseSchema() {
    db.update(UsersDb.groupsTableDml);
    db.update(UsersDb.usersTableDml);
    final var repo = new PostgreSqlUsersRepository(db);
    repo.createGroup("swqa");
    repo.createUser("John", "Doe", "john.doe@example.com", "student", "swqa");
    repo.createUser("Jane", "Doe", "jane.doe@example.com", "student", "swqa");
    repo.createUser("Mariah", "Harris", "mariah.hairam@example.com", "teacher", "swqa");
    this.app = new CampusApp(repo, new ConsoleEmailSender());
  }

  @Test
  public void testCreateGroup(){
    app.createGroup("bigdata");

  }

  @Test
  public void testSendEmailToGroup() {
    app.sendMailToGroup("swqa", "New campus!", "Hello everyone! We just created a new virtual campus!");
  }

  @Test
  public void testSendEmailToGroupRole() {
    app.sendMailToGroupRole("swqa", "teacher", "Hey! Teacher!", "Let them students alone!!");
  }

  //Write a test for CampusApp::createUser in CampusAppEndToEndTest in a way that solves the issue described in question 1.
  // Pay attention to the strategy and patterns you use.
  @Test
  public void testCreateUser() {
    final var name = "Peter";
    final var surname = "Parker";
    final var email = "peter.parker@example.com";
    final var role = "student";
    final var groupName = "swqa";
    app.createUser(name, surname, email, role, groupName);
    final var users = app.getUsersByGroupAndRole(groupName, role);
    final var user = users.get(2);
    assertEquals(3, users.size());
    assertEquals(name, user.getName());
    assertEquals(surname, user.getSurname());
    assertEquals(email, user.getEmail());
    assertEquals(role, user.getRole());
    assertEquals(groupName, user.getGroupName());
  }

}
