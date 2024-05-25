package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.File;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import static org.assertj.core.api.Assertions.*;

class Sql2oFileRepositoryTest {

    private static Sql2oFileRepository sql2oFileRepository;

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
        sql2oFileRepository = new Sql2oFileRepository(sql2o);
    }

    @AfterEach
    public void clearGenres() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM files").executeUpdate();
            connection.createQuery("ALTER TABLE files ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
    }

    @Test
    public void whenTableExistThreeFilesThenAllFiles() {
        try (var connection = sql2o.open()) {
            String sql = """
                        INSERT INTO files(name, path)
                        VALUES
                        ('image1.jpg', 'files/image1.jpg'),
                        ('image2.jpg', 'files/image2.jpg'),
                        ('image3.jpg', 'files/image3.jpg')
                    """;
            connection.createQuery(sql).executeUpdate();
        }
        var f1 = new File(1, "image1.jpg", "files/image1.jpg");
        var f2 = new File(2, "image2.jpg", "files/image2.jpg");
        var f3 = new File(3, "image3.jpg", "files/image3.jpg");
        assertThat(sql2oFileRepository.findAll()).usingRecursiveComparison().isEqualTo(List.of(f1, f2, f3));
    }

    @Test
    public void whenEmptyTableThenEmptyCollection() {
        assertThat(sql2oFileRepository.findAll()).isEqualTo(List.of());
    }

    @Test
    public void whenFindByIdThenFile() {
        try (var connection = sql2o.open()) {
            String sql = "INSERT INTO files(name, path) VALUES ('image1.jpg', 'files/image1.jpg')";
            connection.createQuery(sql).executeUpdate();
        }
        var f = new File(1, "image1.jpg", "files/image1.jpg");
        assertThat(sql2oFileRepository.findById(1)).usingRecursiveComparison().isEqualTo(Optional.of(f));
    }

    @Test
    public void whenNotExistFileThenEmptyOptional() {
        assertThat(sql2oFileRepository.findById(1)).isEqualTo(Optional.empty());
    }
}