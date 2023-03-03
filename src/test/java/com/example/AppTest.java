package com.example;

import edu.upc.talent.swqa.jdbc.Jdbc;
import edu.upc.talent.swqa.jdbc.RowReader;
import org.junit.jupiter.api.Test;

import static edu.upc.talent.swqa.jdbc.Param.p;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MyFirstJUnitJupiterTests {

  @Test
  void addition() {
    var jdbc = new Jdbc(null);
    RowReader<String> readUser = (rs) -> rs.getString(1);
    jdbc.inTransaction((tx) ->
          tx.select("select name from users where department = ? and salary < ?", readUser, p("sales"), p(40_000))
    );
    assertEquals(2, 1 + 3);
  }

}

