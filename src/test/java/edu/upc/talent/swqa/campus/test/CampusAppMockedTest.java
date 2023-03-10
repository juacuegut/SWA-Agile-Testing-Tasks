package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.CampusApp;
import edu.upc.talent.swqa.campus.domain.EmailSender;
import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.function.BiConsumer;

public final class CampusAppMockedTest {

  final List<User> initialUsers = List.of(
        new User("1", "John", "Doe", "john.doe@example.com", "student", "swqa"),
        new User("2", "Jane", "Doe", "jane.doe@example.com", "student", "swqa"),
        new User("3", "Mariah", "Harris", "mariah.hairam@example.com", "teacher", "swqa")
  );

  private void test(final BiConsumer<CampusApp, EmailSender> test) {
    final var usersRepository = mock(UsersRepository.class);
    final var emailSender = mock(EmailSender.class);
    final var app = new CampusApp(usersRepository, emailSender);
    when(usersRepository.getUsersByGroup("swqa")).thenReturn(initialUsers);
    when(usersRepository.getUsersByGroupAndRole("swqa", "teacher")).thenReturn(List.of(initialUsers.get(2)));
    try {
      test.accept(app, emailSender);
    } catch (final Exception e) {
      fail(e);
    }
  }

  @Test
  public void testSendEmailToGroup() {
    test((app, emailSender) -> {
      final var subject = "New campus!";
      final var body = "Hello everyone! We just created a new virtual campus!";
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
      final var subject = "Hey! Teacher!";
      final var body = "Let them students alone!!";
      app.sendMailToGroupRole("swqa", "teacher", subject, body);
      verify(emailSender).sendEmail("mariah.hairam@example.com", subject, body);
      verifyNoMoreInteractions(emailSender);
    });
  }
}
