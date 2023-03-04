package edu.upc.talent.swqa.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class HikariCP {
  public static HikariDataSource getDataSource(String jdbcUrl, String username, String password) {
    var config = new HikariConfig();
    config.setJdbcUrl(jdbcUrl);
    config.setUsername(username);
    config.setPassword(password);
    config.setDriverClassName(org.postgresql.Driver.class.getName());
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    return new HikariDataSource(config);
  }


}
