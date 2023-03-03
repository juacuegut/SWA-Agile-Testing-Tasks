package edu.upc.talent.swqa.functions;

import java.util.function.BiFunction;

import static edu.upc.talent.swqa.functions.Utils.failOnException;

@FunctionalInterface
public interface Function2<A, B, R> {

  R invoke(A a, B b) throws Exception;

  default R apply(A a, B b) {
    return Utils.failOnException(() -> invoke(a,b));
  }

  default BiFunction<A, B, R> toBiFunction() {
    return (a, b) -> Utils.failOnException(() -> apply(a, b));
  }

  default BiFunction<A, B, R> withWrappedExceptions() {
    return toBiFunction();
  }

}