package ru.job4j.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmPreview;
import ru.job4j.cinema.model.Film;
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

    @Override
    public Optional<FilmPreview> findById(int id) {
        Optional<Film> filmOptional = filmRepository.findById(id);
        if (filmOptional.isPresent()) {
            var film = filmOptional.get();
            var genre = genreRepository.findById(film.getGenreId()).get();
            var file = fileRepository.findById(film.getFileId()).get();
            return Optional.of(FilmPreview.builder()
                            .name(film.getName())
                            .description(film.getDescription())
                            .year(film.getYear())
                            .genre(genre.getName())
                            .minimalAge(film.getMinimalAge())
                            .durationInMinutes(film.getDurationInMinutes())
                            .fileName(file.getName())
                            .filePath(file.getPath())
                    .build());
        }
        return Optional.empty();
    }

    @Override
    public Map<Integer, FilmPreview> findAll() {
        var genres = genreService.findAll();
        var files = fileService.findAll();
        return filmRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(Film::getId, (film) -> FilmPreview.builder()
                        .name(film.getName())
                        .description(film.getDescription())
                        .year(film.getYear())
                        .genre(genres.get(film.getGenreId()).getName())
                        .minimalAge(film.getMinimalAge())
                        .durationInMinutes(film.getDurationInMinutes())
                        .fileName(files.get(film.getFileId()).getName())
                        .filePath(files.get(film.getFileId()).getPath())
                        .build()));
    }
}
