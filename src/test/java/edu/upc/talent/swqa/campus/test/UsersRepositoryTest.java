package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import edu.upc.talent.swqa.campus.test.utils.Group;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static edu.upc.talent.swqa.util.Utils.union;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface UsersRepositoryTest {
  UsersRepositoryState testAndGetFinalState(String testName, UsersRepositoryState initialState, Consumer<UsersRepository> test);

  default void test(String testName, UsersRepositoryState initialState, UsersRepositoryState expectedFinalState, Consumer<UsersRepository> test) {
    var finalState = testAndGetFinalState(testName, initialState, test);
    assertEquals(expectedFinalState, finalState);
  }


  Set<User> initialUsers = Set.of(
        new User("1", "John", "Doe", "john.doe@example.com", "student", "swqa"),
        new User("2", "Jane", "Doe", "jane.doe@example.com", "student", "swqa"),
        new User("3", "Mariah", "Hairam", "mariah.hairam@example.com", "teacher", "swqa")
  );

  UsersRepositoryState initialState = new UsersRepositoryState(
        Set.of(
              new User("1", "John", "Doe", "john.doe@example.com", "student", "swqa"),
              new User("2", "Jane", "Doe", "jane.doe@example.com", "student", "swqa"),
              new User("3", "Mariah", "Hairam", "mariah.hairam@example.com", "teacher", "swqa")
        ),
        Set.of(new Group(1, "swqa"))
  );


  @Test
  default void testGetUsersByGroup() {
    test("testGetUsersByGroup", initialState, initialState, (repository) -> {
      var actual = repository.getUsersByGroup("swqa");
      assertEquals(initialUsers, new HashSet<>(actual));
    });
  }

  @Test
  default void testCreateUser() {
    var name = "Jack";
    var surname = "Doe";
    var email = "jack.doe@example.com";
    var role = "student";
    var groupName = "swqa";
    var expectedNewUser = new User("4", name, surname, email, role, groupName);
    var expected = new UsersRepositoryState(union(initialUsers, Set.of(expectedNewUser)), initialState.groups());
    test("testCreateUser", initialState, expected, (repository) ->
          repository.createUser(name, surname, email, role, groupName)
    );
  }

  @Test @Disabled
  default void testCreateUserFailsIfGroupDoesNotExist() {
    test("testCreateUser", initialState, initialState, (repository) -> {
      var groupName = "non-existent";
      var exception = assertThrows(RuntimeException.class, () ->
            repository.createUser("a", "b", "a.b@example.com", "student", groupName)
      );
      assertEquals("Group " + groupName + " does not exist", exception.getMessage());
    });
  }

}



