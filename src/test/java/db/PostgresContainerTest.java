package db;

import io.qameta.allure.Step;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Execution(ExecutionMode.SAME_THREAD)
@Testcontainers
public class PostgresContainerTest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

    private static HikariDataSource dataSource;


    @BeforeAll
    static void setup() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgres.getJdbcUrl());
        config.setUsername(postgres.getUsername());
        config.setPassword(postgres.getPassword());
        config.setMaximumPoolSize(2);
        dataSource = new HikariDataSource(config);

        try (Connection liquibaseConnection = DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()
        )) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(liquibaseConnection));
            try (Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.xml",
                    new ClassLoaderResourceAccessor(),
                    database
            )) {
                liquibase.update();
            }
        }
    }

    @AfterAll
    static void tearDown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    @Step("Проверка SQL по категориям товаров")
    void shouldFindAllDescendantsOfSauce() throws SQLException {
        String sql = """
                WITH RECURSIVE category_tree AS (
                    SELECT
                        id,
                        name,
                        parent_id
                    FROM
                        product_categories
                    WHERE
                        name = 'Sauce'
                    UNION ALL
                    SELECT
                        pc.id,
                        pc.name,
                        pc.parent_id
                    FROM
                        product_categories pc JOIN
                        category_tree ct ON pc.parent_id = ct.id)
                SELECT name FROM category_tree ORDER BY id""";

        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql))  {

            List<String> names = new ArrayList<>();
            while (resultSet.next()) {
                names.add(resultSet.getString("name"));
            }

            assertEquals(List.of("Sauce", "Hot Sauce", "Extra Hot"), names);
        }
    }

    @Test
    @Step("Проверка SQL по заказам")
    void shouldCalculateTotalSpentPerUser() throws SQLException {
        String sql = """
                SELECT
                    u.username,
                    SUM(o.total) AS total_spent
                FROM
                    users   u JOIN
                    orders  o ON u.id = o.user_id
                GROUP BY u.username
                ORDER BY total_spent DESC""";

        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            Map<String, Double> totals = new LinkedHashMap<>();

            while (resultSet.next()) {
                totals.put(resultSet.getString("username"), resultSet.getDouble("total_spent"));
            }

            assertEquals(2, totals.size());
            assertEquals(99.99, totals.get("locked_out_user"), 0.01);
            assertEquals(35.50, totals.get("standard_user"), 0.01);
        }
    }
}
