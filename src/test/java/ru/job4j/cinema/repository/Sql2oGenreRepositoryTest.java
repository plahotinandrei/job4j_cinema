package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Genre;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import static org.assertj.core.api.Assertions.*;

class Sql2oGenreRepositoryTest {

    private static Sql2oGenreRepository sql2oGenreRepository;

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
        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
    }

    @AfterEach
    public void clearGenres() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM genres").executeUpdate();
            connection.createQuery("ALTER TABLE genres ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
    }

    @Test
    public void whenTableExistThreeGenresThenAllGenres() {
        try (var connection = sql2o.open()) {
            String sql = "INSERT INTO genres(name) VALUES ('Боевик'), ('Драма'), ('Комедия')";
            connection.createQuery(sql).executeUpdate();
        }
        var g1 = new Genre(1, "Боевик");
        var g2 = new Genre(2, "Драма");
        var g3 = new Genre(3, "Комедия");
        assertThat(sql2oGenreRepository.findAll()).usingRecursiveComparison().isEqualTo(List.of(g1, g2, g3));
    }

    @Test
    public void whenEmptyTableThenEmptyCollection() {
        assertThat(sql2oGenreRepository.findAll()).isEqualTo(List.of());
    }

    @Test
    public void whenFindByIdThenGenre() {
        try (var connection = sql2o.open()) {
            String sql = "INSERT INTO genres(name) VALUES ('Боевик')";
            connection.createQuery(sql).executeUpdate();
        }
        var g = new Genre(1, "Боевик");
        assertThat(sql2oGenreRepository.findById(1)).usingRecursiveComparison().isEqualTo(Optional.of(g));
    }

    @Test
    public void whenNotExistGenreThenEmptyOptional() {
        assertThat(sql2oGenreRepository.findById(1)).isEqualTo(Optional.empty());
    }
}