package edu.upc.talent.swqa.campus.domain;

import java.util.List;

public interface UsersRepository {
  void createUser(String name, String surname, String email, String role, String groupName);
  void createGroup(String name);

  List<User> getUsersByGroupAndRole(String groupName, String onlyRole);

  List<User> getUsersByGroup(String groupName);
}
