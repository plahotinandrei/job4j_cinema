package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.FilmPreview;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.FileRepository;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFilmServiceTest {

    private static FilmRepository filmRepository;

    private static GenreRepository genreRepository;

    private static GenreService genreService;

    private static FileRepository fileRepository;

    private static FileService fileService;

    private static FilmService filmService;

    @BeforeAll
    public static void init() {
        filmRepository = mock(FilmRepository.class);
        var film1 = new Film(1, "Аватар", "Описание фильма аватар", 2009, 1, 12, 179, 1);
        var film2 = new Film(2, "Дюна", "Описание фильма дюна", 2021, 2, 12, 155, 2);
        var film3 = new Film(3, "Грань будущего", "Описание фильма грань будущего", 2014, 3, 12, 113, 3);
        when(filmRepository.findAll()).thenReturn(List.of(film1, film2, film3));
        when(filmRepository.findById(1)).thenReturn(Optional.of(film1));
        when(filmRepository.findById(2)).thenReturn(Optional.of(film2));
        when(filmRepository.findById(3)).thenReturn(Optional.of(film3));
        genreRepository = mock(GenreRepository.class);
        var genre1 = new Genre(1, "Боевик");
        var genre2 = new Genre(2, "Комедия");
        var genre3 = new Genre(3, "Драма");
        when(genreRepository.findAll()).thenReturn(List.of(genre1, genre2, genre3));
        when(genreRepository.findById(1)).thenReturn(Optional.of(genre1));
        when(genreRepository.findById(2)).thenReturn(Optional.of(genre2));
        when(genreRepository.findById(3)).thenReturn(Optional.of(genre3));
        genreService = mock(GenreService.class);
        when(genreService.findAll()).thenReturn(Map.of(1, genre1, 2, genre2, 3, genre3));
        fileRepository = mock(FileRepository.class);
        var file1 = new File(1, "image1.jpg", "files/image1.jpg");
        var file2 = new File(2, "image2.jpg", "files/image2.jpg");
        var file3 = new File(3, "image3.jpg", "files/image3.jpg");
        when(fileRepository.findAll()).thenReturn(List.of(file1, file2, file3));
        when(fileRepository.findById(1)).thenReturn(Optional.of(file1));
        when(fileRepository.findById(2)).thenReturn(Optional.of(file2));
        when(fileRepository.findById(3)).thenReturn(Optional.of(file3));
        fileService = mock(FileService.class);
        when(fileService.findAll()).thenReturn(Map.of(1, file1, 2, file2, 3, file3));
        filmService = new SimpleFilmService(filmRepository, genreRepository, genreService, fileRepository, fileService);
    }

    @Test
    public void whenRepositoryExistThreeFilmsThenMapWithThreeFilms() {
        var filmPreview1 = FilmPreview.builder()
                .name("Аватар").description("Описание фильма аватар").year(2009)
                .genre("Боевик").minimalAge(12).durationInMinutes(179)
                .fileName("image1.jpg").filePath("files/image1.jpg")
                .build();
        var filmPreview2 = FilmPreview.builder()
                .name("Дюна").description("Описание фильма дюна").year(2021)
                .genre("Комедия").minimalAge(12).durationInMinutes(155)
                .fileName("image2.jpg").filePath("files/image2.jpg")
                .build();
        var filmPreview3 = FilmPreview.builder()
                .name("Грань будущего").description("Описание фильма грань будущего").year(2014)
                .genre("Драма").minimalAge(12).durationInMinutes(113)
                .fileName("image3.jpg").filePath("files/image3.jpg")
                .build();
        assertThat(filmService.findAll()).usingRecursiveComparison()
                .isEqualTo(Map.of(1, filmPreview1, 2, filmPreview2, 3, filmPreview3));
    }

    @Test
    public void whenFindByIdThenFilmPreview() {
        var filmPreview = FilmPreview.builder()
                .name("Дюна").description("Описание фильма дюна").year(2021)
                .genre("Комедия").minimalAge(12).durationInMinutes(155)
                .fileName("image2.jpg").filePath("files/image2.jpg")
                .build();
        assertThat(filmService.findById(2)).usingRecursiveComparison().isEqualTo(Optional.of(filmPreview));
    }

    @Test
    public void whenNotExistFilmThenEmptyOptional() {
        assertThat(filmService.findById(4)).isEqualTo(Optional.empty());
    }
}