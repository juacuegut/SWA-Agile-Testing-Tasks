package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;

import java.util.List;

public class InMemoryUsersRepository implements UsersRepository {

  private UsersRepositoryState state;

  public InMemoryUsersRepository(UsersRepositoryState state) {
    this.state = state;
  }

  @Override
  public void createUser(String name, String surname, String email, String role, String groupName) {
    Integer id = state.users().size() + 1;
    var user = new User(id.toString(), name, surname, email, role, groupName);
    state.users().add(user);
  }

  @Override
  public void createGroup(String name) {
    Integer id = state.groups().size() + 1;
    state.groups().add(new Group(id, name));
  }

  @Override
  public List<User> getUsersByGroupAndRole(String groupName, String onlyRole) {
    return state.users().stream()
          .filter(user -> user.groupName().equals(groupName) && user.role().equals(onlyRole))
          .toList();
  }

  @Override
  public List<User> getUsersByGroup(String groupName) {
    return state.users().stream()
          .filter(user -> user.groupName().equals(groupName))
          .toList();
  }
}
