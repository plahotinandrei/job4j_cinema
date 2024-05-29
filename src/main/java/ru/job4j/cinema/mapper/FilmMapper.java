package ru.job4j.cinema.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.cinema.dto.FilmPreview;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;

@Mapper
public interface FilmMapper {

    @Mapping(source = "film.name", target = "name")
    @Mapping(source = "genre.name", target = "genre")
    @Mapping(source = "file.name", target = "fileName")
    @Mapping(source = "file.path", target = "filePath")
    FilmPreview getFilmPreview(Film film, Genre genre, File file);
}
