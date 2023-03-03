package edu.upc.talent.swqa.jdbc;

import edu.upc.talent.swqa.functions.Function1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static edu.upc.talent.swqa.functions.Utils.failOnException;

public class ConnectionScope {
  private final Connection connection;

  public ConnectionScope(Connection connection) {
    this.connection = connection;
  }

  public <A> List<A> select(String sql, RowReader<A> reader, Param... params) {
    return withStatement(sql, (stmt) -> {
      try (var rs = stmt.executeQuery()) {
        var res = new LinkedList<A>();
        while (rs.next()) {
          res.add(reader.apply(new ResultSetView(rs)));
        }
        return Collections.unmodifiableList(res);
      }
    }, params);
  }

  public int update(String sql, Param... params) {
    return withStatement(sql, PreparedStatement::executeUpdate, params);
  }

  private <A> A withStatement(String sql, Function1<PreparedStatement, A> program, Param... params) {
    return failOnException(() -> {
      try (var stmt = connection.prepareStatement(sql)) {
        for (int i = 0; i < params.length; i++) params[i].set(stmt, i + 1);
        return program.apply(stmt);
      }
    });
  }
}
