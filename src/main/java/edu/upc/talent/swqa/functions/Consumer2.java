package edu.upc.talent.swqa.functions;

import java.util.function.BiConsumer;

import static edu.upc.talent.swqa.functions.Utils.failOnException;

@FunctionalInterface
public interface Consumer2<A, B> {

  void invoke(A a, B b) throws Exception;

  default void apply(A a, B b) {
    failOnException(() -> invoke(a, b));
  }

  default BiConsumer<A,B> toBiConsumer() {
    return (a,b) -> apply(a,b);
  }

  default BiConsumer<A, B> withWrappedExceptions() {
    return toBiConsumer();
  }

}
