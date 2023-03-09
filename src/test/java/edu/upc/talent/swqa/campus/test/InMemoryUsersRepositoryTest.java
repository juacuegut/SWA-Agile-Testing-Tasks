package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.UsersRepository;
import edu.upc.talent.swqa.campus.test.utils.InMemoryUsersRepository;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;

import java.util.function.Consumer;

class InMemoryUsersRepositoryTest implements UsersRepositoryTest {

  @Override
  public UsersRepositoryState testAndGetFinalState(String testName, UsersRepositoryState initialState, Consumer<UsersRepository> test) {
    var state = initialState.copy();
    var repository = new InMemoryUsersRepository(state);
    test.accept(repository);
    return state;
  }
}
