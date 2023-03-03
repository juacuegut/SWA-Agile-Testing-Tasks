package edu.upc.talent.swqa.functions;

import static edu.upc.talent.swqa.functions.Utils.failOnException;

@FunctionalInterface
public interface Function3<A, B, C, R> {

  R invoke(A a, B b, C c) throws Exception;

  default R apply(A a, B b, C c) {
    return Utils.failOnException(() -> invoke(a, b, c));
  }

}