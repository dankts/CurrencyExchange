package org.dankts.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.sql.Connection;

@UtilityClass
public class ConnectionManager {

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(PropertiesUtil.getValues("db.url"));
        config.setUsername(PropertiesUtil.getValues("db.username"));
        config.setPassword(PropertiesUtil.getValues("db.password"));
        config.setDriverClassName("org.postgresql.Driver");

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(3);
        config.setIdleTimeout(60000);
        config.setMaxLifetime(3600000);
        config.setConnectionTimeout(5000);

        dataSource = new HikariDataSource(config);
    }

    @SneakyThrows
    public static Connection getConnection() {
        return dataSource.getConnection();
    }
}
