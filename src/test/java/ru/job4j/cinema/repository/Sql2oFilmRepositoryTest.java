package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Film;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import static org.assertj.core.api.Assertions.*;

class Sql2oFilmRepositoryTest {

    private static Sql2oFilmRepository sql2oFilmRepository;

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
        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        try (var connection = sql2o.open()) {
            String sql = """
                        INSERT INTO files(name, path)
                        VALUES
                        ('image1.jpg', 'files/image1.jpg'),
                        ('image2.jpg', 'files/image2.jpg'),
                        ('image3.jpg', 'files/image3.jpg');
                        INSERT INTO genres(name) VALUES ('Боевик'), ('Драма'), ('Комедия');
                    """;
            connection.createQuery(sql).executeUpdate();
        }
    }

    @AfterEach
    public void clearFilms() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM films").executeUpdate();
            connection.createQuery("ALTER TABLE films ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
    }

    @AfterAll
    public static void clearFilesAndGenres() {
        try (var connection = sql2o.open()) {
            String sql = """
                        DELETE FROM files;
                        ALTER TABLE files ALTER COLUMN id RESTART WITH 1;
                        DELETE FROM genres;
                        ALTER TABLE genres ALTER COLUMN id RESTART WITH 1;
                    """;
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void whenTableExistThreeFilmsThenAllFilms() {
        try (var connection = sql2o.open()) {
            String sql = """
                        INSERT INTO films(name, description, "year", genre_id, minimal_age, duration_in_minutes, file_id)
                        VALUES
                        ('Аватар', 'Описание фильма аватар', 2009, 1, 12, 179, 1),
                        ('Дюна', 'Описание фильма дюна', 2021, 2, 12, 155, 2),
                        ('Грань будущего', 'Описание фильма грань будущего', 2014, 3, 12, 113, 3)
                    """;
            connection.createQuery(sql).executeUpdate();
        }
        var f1 = new Film(1, "Аватар", "Описание фильма аватар", 2009, 1, 12, 179, 1);
        var f2 = new Film(2, "Дюна", "Описание фильма дюна", 2021, 2, 12, 155, 2);
        var f3 = new Film(3, "Грань будущего", "Описание фильма грань будущего", 2014, 3, 12, 113, 3);
        assertThat(sql2oFilmRepository.findAll()).usingRecursiveComparison().isEqualTo(List.of(f1, f2, f3));
    }

    @Test
    public void whenEmptyTableThenEmptyCollection() {
        assertThat(sql2oFilmRepository.findAll()).isEqualTo(List.of());
    }

    @Test
    public void whenFindByIdThenFilm() {
        try (var connection = sql2o.open()) {
            String sql = """
                        INSERT INTO films(name, description, "year", genre_id, minimal_age, duration_in_minutes, file_id)
                        VALUES ('Аватар', 'Описание фильма аватар', 2009, 1, 12, 179, 1)
                    """;
            connection.createQuery(sql).executeUpdate();
        }
        var f = new Film(1, "Аватар", "Описание фильма аватар", 2009, 1, 12, 179, 1);
        assertThat(sql2oFilmRepository.findById(1)).usingRecursiveComparison().isEqualTo(Optional.of(f));
    }

    @Test
    public void whenNotExistFilmThenEmptyOptional() {
        assertThat(sql2oFilmRepository.findById(1)).isEqualTo(Optional.empty());
    }
}