package edu.upc.talent.swqa.functions;

import java.util.function.Consumer;

import static edu.upc.talent.swqa.functions.Utils.failOnException;

@FunctionalInterface
public interface Consumer1<A> {

  void invoke(A a) throws Exception;

  default void apply(A a) {
    failOnException(() -> invoke(a));
  }

  default Consumer<A> toConsumer() {
    return a -> apply(a);
  }

  default Consumer<A> withWrappedExceptions() {
    return toConsumer();
  }

}
