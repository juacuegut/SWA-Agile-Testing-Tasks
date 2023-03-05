package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.CampusApp;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CampusAppTest {

  private void test(BiConsumer<CampusAppState, CampusApp> test) {
    var campusAppState = new CampusAppState();
    var usersRepository = new InMemoryUsersRepository(campusAppState.usersRepository());
    var emailSender = new InMemoryEmailSender(campusAppState.sentEmails());
    var app = new CampusApp(usersRepository, emailSender);
    app.createGroup("swqa");
    app.createUser("John", "Doe", "john.doe@example.com", "student", "swqa");
    app.createUser("Jane", "Doe", "jane.doe@example.com", "student", "swqa");
    app.createUser("Mariah", "Hairam", "mariah.hairam@example.com", "teacher", "swqa");
    try {
      test.accept(campusAppState, app);
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  public void testSendEmailToGroup() {
    test((state, app) -> {
      var subject = "New campus!";
      var body = "Hello everyone! We just created a new virtual campus!";
      app.sendMailToGroup("swqa", subject, body);
      var expected = Set.of(
            new SentEmail("john.doe@example.com", subject, body),
            new SentEmail("jane.doe@example.com", subject, body),
            new SentEmail("mariah.hairam@example.com", subject, body));
      var actual = new HashSet<>(state.sentEmails());
      assertEquals(expected, actual);
    });
  }

  @Test
  public void testSendEmailToGroupRole() {
    test((state, app) -> {
      var subject = "Hey! Teacher!";
      var body = "Let them students alone!!";
      app.sendMailToGroupRole("swqa", "teacher", subject, body);
      var expected = Set.of(new SentEmail("mariah.hairam@example.com", subject, body));
      var actual = new HashSet<>(state.sentEmails());
      assertEquals(expected, actual);
    });
  }
}
