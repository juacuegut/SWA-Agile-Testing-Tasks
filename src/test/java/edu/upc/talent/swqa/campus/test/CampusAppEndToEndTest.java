package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.CampusApp;
import edu.upc.talent.swqa.campus.infrastructure.ConsoleEmailSender;
import edu.upc.talent.swqa.campus.infrastructure.PostgreSqlUsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.UsersDb;
import edu.upc.talent.swqa.jdbc.Database;
import edu.upc.talent.swqa.test.utils.DatabaseTest;
import org.junit.jupiter.api.Test;

public class CampusAppEndToEndTest extends DatabaseTest {
  @Override
  protected void setUpDatabaseSchema(Database db) {
    db.update(UsersDb.groupsTableDml);
    db.update(UsersDb.usersTableDml);
  }

  @Override
  public void beforeEach(Database db) {
    var repo = new PostgreSqlUsersRepository(db);
    repo.createGroup("swqa");
    repo.createUser("John", "Doe", "john.doe@example.com", "student", "swqa");
    repo.createUser("Jane", "Doe", "jane.doe@example.com", "student", "swqa");
    repo.createUser("Mariah", "Hairam", "mariah.hairam@example.com", "teacher", "swqa");

  }

  private CampusApp app(Database db) {
    return new CampusApp(new PostgreSqlUsersRepository(db), new ConsoleEmailSender());
  }

  @Test
  public void testSendEmailToGroup() {
    databaseTestVoid("sendEmailToGroup", (db) -> {
            app(db).sendMailToGroup("swqa", "New campus!", "Hello everyone! We just created a new virtual campus!");
          }
    );
  }

  @Test
  public void testSendEmailToGroupRole() {
    databaseTestVoid("sendEmailToGroupRole", (db) -> {
            app(db).sendMailToGroupRole("swqa", "teacher", "Hey! Teacher!", "Let them students alone!!");
          }
    );
  }

}
