package edu.upc.talent.swqa.campus.test.utils;

import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;

@FunctionalInterface
public interface UsersRepositoryStateGetter {

  UsersRepositoryState get();

}