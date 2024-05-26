package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.GenreRepository;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleGenreServiceTest {

    private static GenreRepository genreRepository;

    private static GenreService genreService;

    @BeforeAll
    public static void init() {
        genreRepository = mock(GenreRepository.class);
        genreService = new SimpleGenreService(genreRepository);
    }

    @Test
    public void whenRepositoryExistThreeGenresThenMapWithThreeGenres() {
        var genre1 = new Genre(1, "Боевик");
        var genre2 = new Genre(2, "Комедия");
        var genre3 = new Genre(3, "Драма");
        when(genreRepository.findAll()).thenReturn(List.of(genre1, genre2, genre3));
        assertThat(genreService.findAll()).usingRecursiveComparison().isEqualTo(Map.of(1, genre1, 2, genre2, 3, genre3));
    }

    @Test
    public void whenRepositoryNotExistGenresThenEmptyMap() {
        when(genreRepository.findAll()).thenReturn(List.of());
        assertThat(genreService.findAll()).usingRecursiveComparison().isEqualTo(Map.of());
    }
}