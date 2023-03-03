package edu.upc.talent.swqa.functions;

import static edu.upc.talent.swqa.functions.Utils.failOnException;

@FunctionalInterface
public interface Consumer3<A, B, C> {

  void invoke(A a, B b, C c) throws Exception;

  default void apply(A a, B b, C c){
     failOnException(() -> invoke(a, b, c));
  }


}
