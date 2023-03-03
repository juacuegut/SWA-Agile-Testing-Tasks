package edu.upc.talent.swqa.functions;

import java.util.function.Function;

import static edu.upc.talent.swqa.functions.Utils.failOnException;

@FunctionalInterface
public interface Function1<A, R> {

  R invoke(A a) throws Exception;

  default R apply(A a) {
    return Utils.failOnException(() -> invoke(a));
  }

  default Function<A, R> toFunction() {
    return (a) -> apply(a);
  }

  default Function<A, R> withWrappedExceptions() {
    return toFunction();
  }

}