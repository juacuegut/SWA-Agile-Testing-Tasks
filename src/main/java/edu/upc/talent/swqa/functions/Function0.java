package edu.upc.talent.swqa.functions;

import java.util.function.Supplier;

import static edu.upc.talent.swqa.functions.Utils.failOnException;

@FunctionalInterface
public interface Function0<T> {

  T invoke() throws Exception;

  default T apply() {
    return Utils.failOnException(() -> invoke());
  }

  default Supplier<T> toSupplier(){
    return () -> apply();
  }

  default Supplier<T> withWrappedExceptions() {
    return toSupplier();
  }


}

