package edu.upc.talent.swqa.jdbc.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static edu.upc.talent.swqa.jdbc.Param.p;
import static edu.upc.talent.swqa.jdbc.test.Utils.initTemplateDatabase;
import static edu.upc.talent.swqa.jdbc.test.Utils.withTestDatabase;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcTest {

  private static String templateDatabaseName = JdbcTest.class.getSimpleName().toLowerCase();

  @BeforeAll
  public static void setup() {
    initTemplateDatabase(templateDatabaseName, (db) ->
          db.withConnection((conn) ->
                conn.update("CREATE TABLE users(id SERIAL PRIMARY KEY, name TEXT, email TEXT)")
          )
    );
  }

  @Test
  public void testInsertAndSelect() {
    withTestDatabase(templateDatabaseName, "test_select", (db) -> {
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
    withTestDatabase(templateDatabaseName, "testInsertAndGetKey", (db) -> {
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
