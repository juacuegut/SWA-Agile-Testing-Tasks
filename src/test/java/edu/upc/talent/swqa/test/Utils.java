package edu.upc.talent.swqa.test;

import edu.upc.talent.swqa.jdbc.Jdbc;

import static edu.upc.talent.swqa.jdbc.HikariCP.getDataSource;

public final class Utils {

  @FunctionalInterface
  interface ThrowingConsumer<A> {
    void accept(A a) throws Exception;
  }

  private static void withDatabase(String databaseName, ThrowingConsumer<Jdbc> f) {
    var jdbcUrl = "jdbc:postgresql://localhost/" + (databaseName == null ? "" : databaseName);
    try (var ds = getDataSource(jdbcUrl, "postgres", "postgres")) {
      f.accept(new Jdbc(ds));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void initTemplateDatabase(String templateDatabaseName, ThrowingConsumer<Jdbc> f) {
    withDatabase(null, (jdbc) -> jdbc.withConnection((conn) -> {
      conn.update("DROP DATABASE IF EXISTS " + templateDatabaseName);
      conn.update("CREATE DATABASE " + templateDatabaseName);
      return true;
    }));
    withDatabase(templateDatabaseName, f);
  }

  public static void withTestDatabase(String templateDatabaseName, String databaseName, ThrowingConsumer<Jdbc> f) {
    var actualDatabaseName = databaseName.toLowerCase();
    withDatabase(null, (jdbc) -> jdbc.withConnection((conn) -> {
      conn.update("DROP DATABASE IF EXISTS " + actualDatabaseName);
      var res = conn.update("CREATE DATABASE " + actualDatabaseName + " TEMPLATE " + templateDatabaseName);
      return res;
    }));
    withDatabase(actualDatabaseName, f);
  }
}
