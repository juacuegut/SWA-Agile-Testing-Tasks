package edu.upc.talent.swqa.test.utils;

import edu.upc.talent.swqa.jdbc.Database;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static edu.upc.talent.swqa.jdbc.HikariCP.getDataSource;
import static edu.upc.talent.swqa.test.utils.ConsoleUtils.printAlignedTable;
import static edu.upc.talent.swqa.util.Utils.join;

public abstract class DatabaseTest {

//  protected String templateDatabaseName = getClass().getSimpleName().toLowerCase();
//
//  @BeforeEach
//  public void setup() {
//    withDatabaseVoid(null, (jdbc) -> jdbc.withConnectionVoid((conn) -> {
//      conn.update("DROP DATABASE IF EXISTS " + templateDatabaseName);
//      conn.update("CREATE DATABASE " + templateDatabaseName);
//    }));
//    withDatabaseVoid(templateDatabaseName, (db) -> setUpDatabaseSchema(db));
//  }

  public void beforeEach(Database db) {
  }

  protected abstract void setUpDatabaseSchema(Database db);

  public void databaseTestVoid(String databaseName, Consumer<Database> f) {
    databaseTest(databaseName, (db) -> {
      f.accept(db);
      return null;
    });
  }

  public <A> A databaseTest(String databaseName, Function<Database, A> f) {
    var actualDatabaseName = databaseName.toLowerCase();
    withDatabaseVoid(null, (db) -> db.withConnection((conn) -> {
      conn.update("DROP DATABASE IF EXISTS " + actualDatabaseName);
      var res = conn.update("CREATE DATABASE " + actualDatabaseName);
      return res;
    }));
    return withDatabase(actualDatabaseName, (db) -> {
      setUpDatabaseSchema(db);
      beforeEach(db);
      try {
        return f.apply(db);
      } catch (Exception e) {
        System.out.println("Test failed! Here you have the DB contents for debugging:");
        printDbContents(db);
        throw e;
      }
    });
  }


  private static void printDbContents(Database database) {
    var tables = database.select("SELECT table_name, table_schema FROM information_schema.tables where table_schema = 'public'", (rs) ->
          rs.getString(1)
    );
    tables.forEach((table) -> printTableContents(database, table));
  }

  private static void printTableContents(Database database, String tableName) {
    var columns = database.select("SELECT column_name FROM information_schema.columns WHERE table_name = '" + tableName + "'", (rs) ->
          rs.getString(1)
    );
    var columnNames = String.join(", ", columns);
    var rows = database.select("SELECT " + columnNames + " FROM " + tableName, (rs) -> {
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

  private static void withDatabaseVoid(String databaseName, Consumer<Database> f) {
    withDatabase(databaseName, (db) -> {
      f.accept(db);
      return null;
    });
  }

  private static <A> A withDatabase(String databaseName, Function<Database, A> f) {
    var jdbcUrl = "jdbc:postgresql:///" + (databaseName == null ? "" : databaseName);
    try (var ds = getDataSource(jdbcUrl, "postgres", "postgres")) {
      return f.apply(new Database(ds));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
