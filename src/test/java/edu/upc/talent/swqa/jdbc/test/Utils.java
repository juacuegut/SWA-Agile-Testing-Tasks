package edu.upc.talent.swqa.jdbc.test;

import edu.upc.talent.swqa.jdbc.Jdbc;

import java.util.ArrayList;
import java.util.List;

import static edu.upc.talent.swqa.jdbc.HikariCP.getDataSource;
import static edu.upc.talent.swqa.util.Utils.*;

public final class Utils {

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

  private static void printTableContents(Jdbc jdbc, String tableName) {
    var columns = jdbc.select("SELECT column_name FROM information_schema.columns WHERE table_name = '" + tableName + "'", (rs) ->
      rs.getString(1)
    );
    var columnNames = String.join(", ", columns);
    var rows = jdbc.select("SELECT " + columnNames + " FROM " + tableName, (rs) -> {
      List<String> row = new ArrayList<>();
      for (int i = 1; i <= columns.size(); i++) {
        row.add(rs.getString(i));
      }
      return row;
    });
    System.out.println(tableName + ":");
    printAlignedTable(join(List.of(columns), rows));
    System.out.println();
  }

  public static void printDbContents(Jdbc jdbc) {
    var tables = jdbc.select("SELECT table_name, table_schema FROM information_schema.tables where table_schema = 'public'", (rs) ->
      rs.getString(1)
    );
    tables.forEach((table) -> printTableContents(jdbc, table));
  }

  public static void printAlignedTable(List<List<String>> table) {
    var columnWidths = new ArrayList<Integer>();
    for (var row : table) {
      for (int i = 0; i < row.size(); i++) {
        var cell = row.get(i);
        if (columnWidths.size() <= i) {
          columnWidths.add(cell.length());
        } else {
          columnWidths.set(i, Math.max(columnWidths.get(i), cell.length()));
        }
      }
    }
    for (var row : table) {
      for (int i = 0; i < row.size(); i++) {
        var cell = row.get(i);
        System.out.print(cell);
        for (int j = 0; j < columnWidths.get(i) - cell.length(); j++) {
          System.out.print(" ");
        }
        System.out.print(" ");
      }
      System.out.println();
    }
  }
}
