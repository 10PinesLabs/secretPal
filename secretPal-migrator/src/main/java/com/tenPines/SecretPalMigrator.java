package com.tenPines;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecretPalMigrator {
    public static void main(String[] args) throws Exception {
        Flyway flyway = new Flyway();
        flyway.setDataSource(System.getenv("JDBC_DATABASE_URL"),
            System.getenv("JDBC_DATABASE_USERNAME"),
            System.getenv("JDBC_DATABASE_PASSWORD"));
        flyway.migrate();
    }
}