package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.CampusApp;
import edu.upc.talent.swqa.campus.domain.EmailSender;
import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class CampusAppMockedTest {

  List<User> initialUsers = List.of(
        new User("1", "John", "Doe", "john.doe@example.com", "student", "swqa"),
        new User("2", "Jane", "Doe", "jane.doe@example.com", "student", "swqa"),
        new User("3", "Mariah", "Hairam", "mariah.hairam@example.com", "teacher", "swqa")
  );

  private void test(BiConsumer<CampusApp, EmailSender> test) {
    var usersRepository = mock(UsersRepository.class);
    var emailSender = mock(EmailSender.class);
    var app = new CampusApp(usersRepository, emailSender);
    when(usersRepository.getUsersByGroup("swqa")).thenReturn(initialUsers);
    when(usersRepository.getUsersByGroupAndRole("swqa", "teacher")).thenReturn(List.of(initialUsers.get(2)));
    try {
      test.accept(app, emailSender);
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  public void testSendEmailToGroup() {
    test((app, emailSender) -> {
      var subject = "New campus!";
      var body = "Hello everyone! We just created a new virtual campus!";
      app.sendMailToGroup("swqa", subject, body);
      verify(emailSender).sendEmail("john.doe@example.com", subject, body);
      verify(emailSender).sendEmail("jane.doe@example.com", subject, body);
      verify(emailSender).sendEmail("mariah.hairam@example.com", subject, body);
      verifyNoMoreInteractions(emailSender);
    });
  }

  @Test
  public void testSendEmailToGroupRole() {
    test((app, emailSender) -> {
      var subject = "Hey! Teacher!";
      var body = "Let them students alone!!";
      app.sendMailToGroupRole("swqa", "teacher", subject, body);
      verify(emailSender).sendEmail("mariah.hairam@example.com", subject, body);
      verifyNoMoreInteractions(emailSender);
    });
  }
}
