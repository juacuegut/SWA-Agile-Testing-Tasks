package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.CampusApp;
import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.test.utils.CampusAppState;
import edu.upc.talent.swqa.campus.test.utils.Group;
import edu.upc.talent.swqa.campus.test.utils.InMemoryEmailSender;
import edu.upc.talent.swqa.campus.test.utils.InMemoryUsersRepository;
import edu.upc.talent.swqa.campus.test.utils.SentEmail;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;
import edu.upc.talent.swqa.test.utils.Asserts;
import org.junit.jupiter.api.Test;

import java.util.Set;

public final class CampusAppTest {

  private CampusAppState state;
  private CampusApp app;

  private final CampusAppState defaultInitialState = new CampusAppState(
        new UsersRepositoryState(
              Set.of(
                    new User("1", "John", "Doe", "john.doe@example.com", "student", "swqa"),
                    new User("2", "Jane", "Doe", "jane.doe@example.com", "student", "swqa"),
                    new User("3", "Mariah", "Harris", "mariah.hairam@example.com", "teacher", "swqa")
              ),
              Set.of(new Group(1, "swqa"))
        ),
        Set.of()
  );

  private void setInitialState(final CampusAppState initialState) {
    this.state = initialState.copy();
    final var usersRepository = new InMemoryUsersRepository(state.usersRepositoryState());
    final var emailSender = new InMemoryEmailSender(state.sentEmails());
    this.app = new CampusApp(usersRepository, emailSender);
  }


  private void assertFinalState(final CampusAppState expectedFinalState) {
    Asserts.assertEquals(expectedFinalState, state);
  }

  @Test
  public void testSendEmailToGroup() {
    setInitialState(defaultInitialState);
    final var subject = "New campus!";
    final var body = "Hello everyone! We just created a new virtual campus!";
    app.sendMailToGroup("swqa", subject, body);
    final var expectedFinalState = new CampusAppState(
          defaultInitialState.usersRepositoryState(),
          Set.of(
                new SentEmail("john.doe@example.com", subject, body),
                new SentEmail("jane.doe@example.com", subject, body),
                new SentEmail("mariah.hairam@example.com", subject, body)
          )
    );
    assertFinalState(expectedFinalState);
  }

  @Test
  public void testSendEmailToGroupRole() {
    setInitialState(defaultInitialState);
    final var subject = "Hey! Teacher!";
    final var body = "Let them students alone!!";
    app.sendMailToGroupRole("swqa", "teacher", subject, body);
    final var expectedFinalState = new CampusAppState(
          defaultInitialState.usersRepositoryState(),
          Set.of(new SentEmail("mariah.hairam@example.com", subject, body))
    );
    assertFinalState(expectedFinalState);

  }
}
