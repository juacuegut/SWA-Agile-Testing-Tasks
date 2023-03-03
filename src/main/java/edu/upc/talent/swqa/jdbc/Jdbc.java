package edu.upc.talent.swqa.jdbc;

import edu.upc.talent.swqa.functions.Function1;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

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
      return f.apply(new ConnectionScope(conn));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}