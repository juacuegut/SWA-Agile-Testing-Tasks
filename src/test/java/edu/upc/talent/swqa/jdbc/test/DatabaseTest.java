package edu.upc.talent.swqa.jdbc.test;

import edu.upc.talent.swqa.jdbc.Database;
import org.junit.jupiter.api.Test;

import java.util.List;

import static edu.upc.talent.swqa.jdbc.Param.p;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest extends edu.upc.talent.swqa.test.utils.DatabaseTest {

  @Override
  protected void setUpDatabaseSchema(Database db) {
    db.update("CREATE TABLE users(id SERIAL PRIMARY KEY, name TEXT, email TEXT)");
  }

  @Test
  public void testInsertAndSelect() {
    databaseTestVoid("testSelect", (db) -> {
      db.update("INSERT INTO users(name, email) VALUES(?,?)", p("John"), p("john@example.com"));
      var actual = db.select(
            "select name, email from users where name = ?",
            (rs) -> rs.getString(1) + " <" + rs.getString(2) + ">",
            p("John")
      );
      assertEquals(List.of("John <john@example.com>"), actual);
    });
  }

  @Test
  public void testInsertAndGetKey() {
    databaseTestVoid("testInsertAndGetKey", (db) -> {
      var key = db.insertAndGetKey(
            "INSERT INTO users(name, email) VALUES(?,?)",
            (rs) -> rs.getInt(1),
            p("John"), p("john@example.com")
      );
      var actual = db.selectOne(
            "select name, email from users where id = ?",
            (rs) -> rs.getString(1) + " <" + rs.getString(2) + ">",
            p(key)
      );
//      System.out.println("Recovered a row with key " + key + " and value " + actual);
      assertEquals("John <john@example.com>", actual);
    });
  }
}
