package edu.upc.talent.swqa.functions;


import static edu.upc.talent.swqa.functions.Utils.failOnException;

/**
 * A Runnable that can throw an exception.
 */
@FunctionalInterface
public interface Consumer0 {

  void invoke() throws Exception;

  default void apply(){
    failOnException(() -> invoke());
  }

  default Runnable toRunnable() {
    return () -> apply();
  }

  default Runnable withWrappedExceptions() {
    return toRunnable();
  }
}
