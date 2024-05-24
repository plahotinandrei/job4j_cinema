package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Hall;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import static org.assertj.core.api.Assertions.*;

class Sql2oHallRepositoryTest {

    private static Sql2oHallRepository sql2oHallRepository;

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
        sql2oHallRepository = new Sql2oHallRepository(sql2o);
    }

    @AfterEach
    public void clearHalls() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM halls").executeUpdate();
            connection.createQuery("ALTER TABLE halls ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
    }

    @Test
    public void whenTableExistThreeHallsThenAllHalls() {
        try (var connection = sql2o.open()) {
            String sql = """
                        INSERT INTO halls(name, row_count, place_count, description)
                        VALUES
                        ('name1', 5, 5, 'description1'),
                        ('name2', 6, 6, 'description2'),
                        ('name3', 7, 7, 'description3')
                    """;
            connection.createQuery(sql).executeUpdate();
        }
        var h1 = new Hall(1, "name1", 5, 5, "description1");
        var h2 = new Hall(2, "name2", 6, 6, "description2");
        var h3 = new Hall(3, "name3", 7, 7, "description3");
        assertThat(sql2oHallRepository.findAll()).usingRecursiveComparison().isEqualTo(List.of(h1, h2, h3));
    }

    @Test
    public void whenEmptyTableThenEmptyCollection() {
        assertThat(sql2oHallRepository.findAll()).isEqualTo(List.of());
    }

    @Test
    public void whenFindByIdThenHall() {
        try (var connection = sql2o.open()) {
            String sql = """
                        INSERT INTO halls(name, row_count, place_count, description)
                        VALUES
                        ('name1', 5, 5, 'description1')
                    """;
            connection.createQuery(sql).executeUpdate();
        }
        var h = new Hall(1, "name1", 5, 5, "description1");
        assertThat(sql2oHallRepository.findById(1)).usingRecursiveComparison().isEqualTo(Optional.of(h));
    }

    @Test
    public void whenNotExistHallThenEmptyOptional() {
        assertThat(sql2oHallRepository.findById(1)).usingRecursiveComparison().isEqualTo(Optional.empty());
    }

}