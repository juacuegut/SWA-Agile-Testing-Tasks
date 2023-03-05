package edu.upc.talent.swqa.campus.test.utils;

import edu.upc.talent.swqa.campus.domain.User;

import java.util.HashSet;
import java.util.Set;

public record UsersRepositoryState(Set<User> users, Set<Group> groups) {
  public UsersRepositoryState() {
    this(new HashSet<>(), new HashSet<>());
  }
}
