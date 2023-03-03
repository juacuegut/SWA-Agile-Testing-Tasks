package edu.upc.talent.swqa.jdbc;

import edu.upc.talent.swqa.functions.Function0;
import edu.upc.talent.swqa.functions.Utils;

import java.sql.ResultSet;

import static edu.upc.talent.swqa.functions.Utils.failOnException;

public class ResultSetView {

  public ResultSetView(ResultSet resultSet) {
    this.resultSet = resultSet;
  }

  private final ResultSet resultSet;

  private boolean wasNull() {
    return Utils.failOnException(resultSet::wasNull);
  }

  private <A> A getPrimitive(Function0<A> f) {
    var result = Utils.failOnException(f);
    return wasNull() ? null : result;
  }

  public Boolean getBoolean(int column) {
    return getPrimitive(() -> resultSet.getBoolean(column));
  }

  public String getString(int column) {
    return Utils.failOnException(() -> resultSet.getString(column));
  }

  public Integer getInt(int column) {
    return getPrimitive(() -> resultSet.getInt(column));
  }

  public Long getLong(int column) {
    return getPrimitive(() -> resultSet.getLong(column));
  }

  public Double getDouble(int column) {
    return getPrimitive(() -> resultSet.getDouble(column));
  }

  public Float getFloat(int column) {
    return getPrimitive(() -> resultSet.getFloat(column));
  }
}
