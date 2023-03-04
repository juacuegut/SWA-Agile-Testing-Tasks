package edu.upc.talent.swqa.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class ConnectionScope {
  private final Connection connection;

  public ConnectionScope(Connection connection) {
    this.connection = connection;
  }

  public <A> List<A> select(String sql, RowReader<A> reader, Param... params) {
    try (var stmt = connection.prepareStatement(sql)) {
      for (int i = 0; i < params.length; i++) params[i].set(stmt, i + 1);
      try (var rs = stmt.executeQuery()) {
        var res = new ArrayList<A>();
        while (rs.next()) {
          res.add(reader.apply(new ResultSetView(rs)));
        }
        return Collections.unmodifiableList(res);
      }
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  public int update(String sql, Param... params) {
    try (var stmt = connection.prepareStatement(sql)) {
      for (int i = 0; i < params.length; i++) params[i].set(stmt, i + 1);
      return stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  public <K> K insertAndGetKey(String sql, RowReader<K> keyReader,  Param... params) {
    try (var stmt = connection.prepareStatement(sql, RETURN_GENERATED_KEYS)) {
      for (int i = 0; i < params.length; i++) params[i].set(stmt, i + 1);
      stmt.executeUpdate();
      try(var keys = stmt.getGeneratedKeys()){
        keys.next();
        return keyReader.apply(new ResultSetView(keys));
      }
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  public <A> A selectOne(String sql, RowReader<A> reader, Param... params) {
    try (var stmt = connection.prepareStatement(sql)) {
      for (int i = 0; i < params.length; i++) params[i].set(stmt, i + 1);
      try (var rs = stmt.executeQuery()) {
        if (rs.next()) {
          return reader.apply(new ResultSetView(rs));
        } else {
          return null;
        }
      }
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }


}
