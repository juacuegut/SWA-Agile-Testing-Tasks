package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.CampusApp;
import edu.upc.talent.swqa.campus.UsersDb;
import edu.upc.talent.swqa.jdbc.test.JdbcTest;
import edu.upc.talent.swqa.jdbc.test.ThrowingConsumer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static edu.upc.talent.swqa.jdbc.test.Utils.*;

public class CampusTest {

  private static String templateDatabaseName = JdbcTest.class.getSimpleName().toLowerCase();

  @BeforeAll
  public static void setup() {
    initTemplateDatabase(templateDatabaseName, (db) -> {
      db.update(UsersDb.groupsTableDml);
      db.update(UsersDb.usersTableDml);
          }
    );
  }

  private void test(String testName, ThrowingConsumer<CampusApp> test) {
    withTestDatabase(templateDatabaseName, testName, (db) -> {
      var app = new CampusApp(db);
      app.createGroup("swqa");
      app.createUser("John", "Doe", "john.doe@example.com", "student", "swqa");
      app.createUser("Jane", "Doe", "jane.doe@example.com", "student", "swqa");
      app.createUser("Mariah", "Hairam", "mariah.hairam@example.com", "teacher", "swqa");
      try {
        test.accept(app);
      } catch (Exception e) {
        System.out.println("Test failed! Here you have the DB contents for debugging:");
        printDbContents(db);
        throw e;
      }
    });
  }

  @Test
  public void testSendEmailToGroup() {
    test("sendEmailToGroup", (app) -> {
      app.sendMailToGroup("swqa", "New campus!", "Hello everyone! We just created a new virtual campus!");
    });
  }

  @Test
  public void testSendEmailToGroupRole() {
    test("sendEmailToGroupRole", (app) -> {
      app.sendMailToGroupRole("swqa", "teacher", "Hey! Teacher!", "Let them students alone!!");
    });
  }

}
