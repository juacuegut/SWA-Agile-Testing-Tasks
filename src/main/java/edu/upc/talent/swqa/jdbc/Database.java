package edu.upc.talent.swqa.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Database {

  private final DataSource dataSource;

  public Database(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void withConnectionVoid(Consumer<ConnectionScope> f){
    withConnection((conn) -> {
      f.accept(conn);
      return null;
    });
  }
  public <A> A withConnection(Function<ConnectionScope, A> f) {
    try (Connection conn = dataSource.getConnection()) {
      return f.apply(new ConnectionScope(conn));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void inTransactionVoid(Consumer<ConnectionScope> f){
    inTransaction((conn) -> {
      f.accept(conn);
      return null;
    });
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


  public <A> Set<A> selectToSet(String sql, RowReader<A> reader, Param... params) {
    return new HashSet<>(select(sql, reader, params));
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