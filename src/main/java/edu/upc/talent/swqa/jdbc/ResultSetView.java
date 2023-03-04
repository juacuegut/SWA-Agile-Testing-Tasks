package edu.upc.talent.swqa.jdbc;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class ResultSetView {

  private final ResultSet resultSet;

  public ResultSetView(ResultSet resultSet) {
    this.resultSet = resultSet;
  }

  public Boolean getBoolean(int column) throws SQLException{
    var res = resultSet.getBoolean(column);
    return resultSet.wasNull() ? null : res;
  }

  public boolean getBool(int column) throws SQLException{
    var res = resultSet.getBoolean(column);
    if(resultSet.wasNull()) throw new SQLException("Column " + column + " is null");
    return res;
  }

  public String getString(int column) throws SQLException {
      return resultSet.getString(column);
  }

  public Integer getInteger(int column)throws SQLException{
    var res = resultSet.getInt(column);
    return resultSet.wasNull() ? null : res;
  }

  public int getInt(int column) throws SQLException{
    var res = resultSet.getInt(column);
    if(resultSet.wasNull()) throw new SQLException("Column " + column + " is null");
    return res;
  }

  public Long getLong(int column)throws SQLException{
    var res = resultSet.getLong(column);
    return resultSet.wasNull() ? null : res;
  }

  public long getLongPrimitive(int column) throws SQLException{
    var res = resultSet.getLong(column);
    if(resultSet.wasNull()) throw new SQLException("Column " + column + " is null");
    return res;
  }

  public Double getDouble(int column)throws SQLException{
    var res = resultSet.getDouble(column);
    return resultSet.wasNull() ? null : res;
  }

  public double getDoublePrimitive(int column) throws SQLException{
    var res = resultSet.getDouble(column);
    if(resultSet.wasNull()) throw new SQLException("Column " + column + " is null");
    return res;
  }

  public Float getFloat(int column)throws SQLException{
    var res = resultSet.getFloat(column);
    return resultSet.wasNull() ? null : res;
  }

  public float getFloatPrimitive(int column) throws SQLException{
    var res = resultSet.getFloat(column);
    if(resultSet.wasNull()) throw new SQLException("Column " + column + " is null");
    return res;
  }

  public URI getURI(int column) throws SQLException {
    var res = resultSet.getString(column);
    return resultSet.wasNull() ? null : URI.create(res);
  }

  public Instant getInstant(int column) throws SQLException {
    var res = resultSet.getTimestamp(column);
    return resultSet.wasNull() ? null : res.toInstant();
  }
}
