package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Session;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import static org.assertj.core.api.Assertions.*;

class Sql2oSessionRepositoryTest {

    private static Sql2oSessionRepository sql2oSessionRepository;

    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepository() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");
        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);
        sql2oSessionRepository = new Sql2oSessionRepository(sql2o);
        try (var connection = sql2o.open()) {
            String sql = """
                        INSERT INTO files(name, path)
                        VALUES
                        ('image1.jpg', 'files/image1.jpg'),
                        ('image2.jpg', 'files/image2.jpg'),
                        ('image3.jpg', 'files/image3.jpg');
                        INSERT INTO genres(name) VALUES ('Боевик'), ('Драма'), ('Комедия');
                        INSERT INTO films(name, description, "year", genre_id, minimal_age, duration_in_minutes, file_id)
                        VALUES
                        ('Аватар', 'Описание фильма аватар', 2009, 1, 12, 179, 1),
                        ('Дюна', 'Описание фильма дюна', 2021, 2, 12, 155, 2),
                        ('Грань будущего', 'Описание фильма грань будущего', 2014, 3, 12, 113, 3);
                        INSERT INTO halls(name, row_count, place_count, description)
                        VALUES
                        ('name1', 5, 5, 'description1'),
                        ('name2', 6, 6, 'description2'),
                        ('name3', 7, 7, 'description3');
                    """;
            connection.createQuery(sql).executeUpdate();
        }
    }

    @AfterEach
    public void clearSessions() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM film_sessions").executeUpdate();
            connection.createQuery("ALTER TABLE film_sessions ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
    }

    @AfterAll
    public static void clearTables() {
        try (var connection = sql2o.open()) {
            String sql = """
                        DELETE FROM films;
                        ALTER TABLE films ALTER COLUMN id RESTART WITH 1;
                        DELETE FROM files;
                        ALTER TABLE files ALTER COLUMN id RESTART WITH 1;
                        DELETE FROM genres;
                        ALTER TABLE genres ALTER COLUMN id RESTART WITH 1;
                        DELETE FROM halls;
                        ALTER TABLE halls ALTER COLUMN id RESTART WITH 1;
                    """;
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void whenTableExistThreeSessionsThenAllSessions() throws Exception {
        try (var connection = sql2o.open()) {
            String sql = """
                        INSERT INTO film_sessions(film_id, halls_id, start_time, end_time, price)
                        VALUES
                        (1, 1, '2024-06-15 8:00:00', '2024-06-15 10:00:00', 500),
                        (2, 2, '2024-06-15 10:00:00', '2024-06-15 12:00:00', 600),
                        (3, 3, '2024-06-15 12:00:00', '2024-06-15 14:00:00', 700)
                    """;
            connection.createQuery(sql).executeUpdate();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        var s1 = new Session(1, 1, 1,
                new Timestamp(dateFormat.parse("2024-06-15 8:00:00").getTime()),
                new Timestamp(dateFormat.parse("2024-06-15 10:00:00").getTime()), 500);
        var s2 = new Session(2, 2, 2,
                new Timestamp(dateFormat.parse("2024-06-15 10:00:00").getTime()),
                new Timestamp(dateFormat.parse("2024-06-15 12:00:00").getTime()), 600);
        var s3 = new Session(3, 3, 3,
                new Timestamp(dateFormat.parse("2024-06-15 12:00:00").getTime()),
                new Timestamp(dateFormat.parse("2024-06-15 14:00:00").getTime()), 700);
        assertThat(sql2oSessionRepository.findAll()).usingRecursiveComparison().isEqualTo(List.of(s1, s2, s3));
    }

    @Test
    public void whenEmptyTableThenEmptyCollection() {
        assertThat(sql2oSessionRepository.findAll()).isEqualTo(List.of());
    }

    @Test
    public void whenFindByIdThenSession() throws Exception {
        try (var connection = sql2o.open()) {
            String sql = """
                        INSERT INTO film_sessions(film_id, halls_id, start_time, end_time, price)
                        VALUES (1, 1, '2024-06-15 8:00:00', '2024-06-15 10:00:00', 500)
                    """;
            connection.createQuery(sql).executeUpdate();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        var s = new Session(1, 1, 1,
                new Timestamp(dateFormat.parse("2024-06-15 8:00:00").getTime()),
                new Timestamp(dateFormat.parse("2024-06-15 10:00:00").getTime()), 500);
        assertThat(sql2oSessionRepository.findById(1)).usingRecursiveComparison().isEqualTo(Optional.of(s));
    }

    @Test
    public void whenNotExistSessionThenEmptyOptional() {
        assertThat(sql2oSessionRepository.findById(1)).isEqualTo(Optional.empty());
    }
}