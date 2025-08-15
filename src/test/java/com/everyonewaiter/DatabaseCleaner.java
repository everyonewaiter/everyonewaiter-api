package com.everyonewaiter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DatabaseCleaner {

  private final JdbcTemplate jdbcTemplate;
  private final RedisConnectionFactory redisConnectionFactory;

  public void clean() {
    cleanMysql();
    cleanRedis();
  }

  private void cleanMysql() {
    jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

    getTruncateQueries().forEach(jdbcTemplate::execute);

    jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
  }

  private void cleanRedis() {
    try (var connection = redisConnectionFactory.getConnection()) {
      connection.serverCommands().flushDb();
    }
  }

  private List<String> getTruncateQueries() {
    return jdbcTemplate.queryForList(
        "select concat('TRUNCATE TABLE ', table_name, ';') as truncate_statement"
            + " from information_schema.tables"
            + " where table_schema = 'everyonewaiter'"
            + " and table_name != 'flyway_schema_history'",
        String.class
    );
  }

}
