package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.User;

import java.util.*;

record Group(int id, String name) {
}

record UsersRepositoryState(Set<User> users, Set<Group> groups){
  public UsersRepositoryState() {
    this(new HashSet<>(), new HashSet<>());
  }
}

record SentEmail(String to, String subject, String body) {
}
public record CampusAppState(UsersRepositoryState usersRepository, List<SentEmail> sentEmails) {
  public CampusAppState() {
    this(new UsersRepositoryState(), new ArrayList<>());
  }
}
