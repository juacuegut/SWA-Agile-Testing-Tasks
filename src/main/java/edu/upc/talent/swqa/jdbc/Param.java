package edu.upc.talent.swqa.jdbc;

import edu.upc.talent.swqa.functions.Consumer3;

import java.sql.PreparedStatement;

import static edu.upc.talent.swqa.functions.Utils.failOnException;

@FunctionalInterface
public interface Param {
  void set(PreparedStatement ps, int i) throws Exception;

  static Param p(Integer value) {
    return param(value, PreparedStatement::setInt);
  }

  static Param p(String value) {
    return param(value, PreparedStatement::setString);
  }

  static Param p(Boolean value) {
    return param(value, PreparedStatement::setBoolean);
  }

  static Param p(Double value) {
    return param(value, PreparedStatement::setDouble);
  }

  static Param p(Float value) {
    return param(value, PreparedStatement::setFloat);
  }

  static Param p(Long value) {
    return param(value, PreparedStatement::setLong);
  }

  private static <A> Param param(A value, Consumer3<PreparedStatement, Integer, A> psFunction) {
    return (ps, i) -> failOnException(() -> psFunction.apply(ps, i, value));
  }

}

