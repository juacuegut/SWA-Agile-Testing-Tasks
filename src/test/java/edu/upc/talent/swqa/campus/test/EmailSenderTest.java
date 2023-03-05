package edu.upc.talent.swqa.campus.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public interface EmailSenderTest {

  @Test
  default void test() {
    fail("Not yet implemented");
  }
}

class InMemoryEmailSenderTest implements EmailSenderTest {

}