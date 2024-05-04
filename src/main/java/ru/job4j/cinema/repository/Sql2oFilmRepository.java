package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import java.sql.ResultSet;
import java.util.Collection;

@Repository
public class Sql2oFilmRepository implements FilmRepository {

    private final Sql2o sql2o;

    public Sql2oFilmRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<Film> findAll() {
        try (var connection = sql2o.open()) {
            String sql = "SELECT films.id, films.name, films.description, films.year, films.minimal_age, "
                    + "films.duration_in_minutes, films.genre_id, g.name AS genre_name, films.file_id, "
                    + "files.name AS file_name, files.path AS file_path "
                    + "FROM films JOIN genres AS g ON films.genre_id = g.id JOIN files ON films.file_id = files.id;";
            var query = connection.createQuery(sql);
            return query.executeAndFetch((ResultSet rs) -> Film.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .year(rs.getInt("year"))
                    .genre(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")))
                    .minimalAge(rs.getInt("minimal_age"))
                    .durationInMinutes(rs.getInt("duration_in_minutes"))
                    .file(new File(rs.getInt("file_id"), rs.getString("file_name"), rs.getString("file_path")))
                    .build());
        }
    }
}
