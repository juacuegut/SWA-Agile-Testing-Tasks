package edu.upc.talent.swqa.jdbc;

import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;

@FunctionalInterface
public interface Param {
  void set(PreparedStatement ps, int i) throws SQLException;

  static Param p(Integer value) {
    return (ps, i) -> ps.setInt(i, value);
  }

  static Param p(int value) {
    return (ps, i) -> ps.setInt(i, value);
  }

  static Param p(String value) {
    return (ps, i) -> ps.setString(i, value);
  }

  static Param p(Boolean value) {
    return (ps, i) -> ps.setBoolean(i, value);
  }

  static Param p(boolean value) {
    return (ps, i) -> ps.setBoolean(i, value);
  }

  static Param p(Double value) {
    return (ps, i) -> ps.setDouble(i, value);
  }

  static Param p(double value) {
    return (ps, i) -> ps.setDouble(i, value);
  }

  static Param p(Float value) {
    return (ps, i) -> ps.setFloat(i, value);
  }

  static Param p(float value) {
    return (ps, i) -> ps.setFloat(i, value);
  }

  static Param p(Long value) {
    return (ps, i) -> ps.setLong(i, value);
  }

  static Param p(long value) {
    return (ps, i) -> ps.setLong(i, value);
  }

  static Param p(URI value) {
    return (ps, i) -> ps.setString(i, value.toString());
  }

  static Param p(Instant value) {
    return (ps, i) -> ps.setTimestamp(i, java.sql.Timestamp.from(value));
  }

}
