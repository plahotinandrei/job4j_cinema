package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Ticket;
import java.util.Optional;
import java.util.Properties;
import static org.assertj.core.api.Assertions.*;

class Sql2oTicketRepositoryTest {

    private static Sql2oTicketRepository sql2oTicketRepository;

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
        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);
        try (var connection = sql2o.open()) {
            String sql = """
                        INSERT INTO files(name, path)
                        VALUES ('image.jpg', 'files/image.jpg');
                        INSERT INTO genres(name) VALUES ('Боевик');
                        INSERT INTO films(name, description, "year", genre_id, minimal_age, duration_in_minutes, file_id)
                        VALUES ('Аватар', 'Описание фильма аватар', 2009, 1, 12, 179, 1);
                        INSERT INTO halls(name, row_count, place_count, description)
                        VALUES ('name', 5, 5, 'description');
                        INSERT INTO film_sessions(film_id, halls_id, start_time, end_time, price)
                        VALUES (1, 1, '2024-06-15 8:00:00', '2024-06-15 10:00:00', 500);
                        INSERT INTO users(full_name, email, password)
                        VALUES ('Иванов Иван', 'ivan@mail.ru', 'pass');
                    """;
            connection.createQuery(sql).executeUpdate();
        }
    }

    @AfterEach
    public void clearTickets() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM tickets").executeUpdate();
            connection.createQuery("ALTER TABLE tickets ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
    }

    @AfterAll
    public static void clearTables() {
        try (var connection = sql2o.open()) {
            String sql = """
                        DELETE FROM film_sessions;
                        ALTER TABLE film_sessions ALTER COLUMN id RESTART WITH 1;
                        DELETE FROM films;
                        ALTER TABLE films ALTER COLUMN id RESTART WITH 1;
                        DELETE FROM files;
                        ALTER TABLE files ALTER COLUMN id RESTART WITH 1;
                        DELETE FROM genres;
                        ALTER TABLE genres ALTER COLUMN id RESTART WITH 1;
                        DELETE FROM halls;
                        ALTER TABLE halls ALTER COLUMN id RESTART WITH 1;
                        DELETE FROM users;
                        ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
                    """;
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void whenSaveTicketThenTicket() {
        var ticket = new Ticket(1, 1, 3, 3, 1);
        assertThat(sql2oTicketRepository.save(ticket)).usingRecursiveComparison().isEqualTo(Optional.of(ticket));
    }

    @Test
    public void whenSaveExistingTicketThenEmptyOptional() {
        var ticket1 = new Ticket(1, 1, 3, 3, 1);
        var ticket2 = new Ticket(1, 1, 3, 3, 1);
        sql2oTicketRepository.save(ticket1);
        assertThat(sql2oTicketRepository.save(ticket2)).isEqualTo(Optional.empty());
    }
}