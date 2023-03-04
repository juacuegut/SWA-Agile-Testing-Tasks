package edu.upc.talent.swqa.jdbc.test;

@FunctionalInterface
public interface ThrowingConsumer<A> {
    void accept(A a) throws Exception;
  }