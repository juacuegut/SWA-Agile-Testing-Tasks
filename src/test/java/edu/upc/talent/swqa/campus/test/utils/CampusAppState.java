package edu.upc.talent.swqa.campus.test.utils;

import java.util.ArrayList;
import java.util.List;

public record CampusAppState(UsersRepositoryState usersRepository, List<SentEmail> sentEmails) {
  public CampusAppState() {
    this(new UsersRepositoryState(), new ArrayList<>());
  }
}


