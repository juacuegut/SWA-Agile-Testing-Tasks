package edu.upc.talent.swqa.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public final class Jdbc {

  private final DataSource dataSource;

  public Jdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public <A> A withConnection(Function<ConnectionScope, A> f) {
    try (Connection conn = dataSource.getConnection()) {
      return f.apply(new ConnectionScope(conn));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public <A> A inTransaction(Function<ConnectionScope, A> f) {
    try (Connection conn = dataSource.getConnection()) {
      conn.setAutoCommit(false);
      var res = f.apply(new ConnectionScope(conn));
      conn.commit();
      return res;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public int update(String sql, Param... params) {
    return withConnection((conn) -> conn.update(sql, params));
  }

  public <A> List<A> select(String sql, RowReader<A> reader, Param... params) {
    return withConnection((conn) -> conn.select(sql, reader, params));
  }

  public <A> A selectOne(String sql, RowReader<A> reader, Param... params) {
    return withConnection((conn) -> conn.selectOne(sql, reader, params));
  }

  public <K> K insertAndGetKey(String sql, RowReader<K> keyReader,  Param... params) {
    return withConnection((conn) -> conn.insertAndGetKey(sql, keyReader, params));
  }
}