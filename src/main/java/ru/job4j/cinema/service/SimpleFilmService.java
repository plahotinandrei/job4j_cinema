package ru.job4j.cinema.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmPreview;
import ru.job4j.cinema.mapper.FilmMapper;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.FileRepository;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimpleFilmService implements FilmService {

    private final FilmRepository filmRepository;

    private final GenreRepository genreRepository;

    private final GenreService genreService;

    private final FileRepository fileRepository;

    private final FileService fileService;

    private final FilmMapper filmMapper = Mappers.getMapper(FilmMapper.class);

    @Override
    public Optional<FilmPreview> findById(int id) {
        var filmOptional = filmRepository.findById(id);
        if (filmOptional.isPresent()) {
            var film = filmOptional.get();
            var genre = genreRepository.findById(film.getGenreId()).orElse(new Genre());
            var file = fileRepository.findById(film.getFileId()).orElse(new File());
            return Optional.of(filmMapper.getFilmPreview(film, genre, file));
        }
        return Optional.empty();
    }

    @Override
    public Map<Integer, FilmPreview> findAll() {
        var genres = genreService.findAll();
        var files = fileService.findAll();
        return filmRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(Film::getId, (film) -> filmMapper
                                .getFilmPreview(film, genres.get(film.getGenreId()), files.get(film.getFileId()))));
    }
}
