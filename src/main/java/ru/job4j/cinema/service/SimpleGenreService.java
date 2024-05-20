package ru.job4j.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.GenreRepository;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimpleGenreService implements GenreService {

    private final GenreRepository repository;

    @Override
    public Map<Integer, Genre> findAll() {
        return repository.findAll().stream()
                .collect(Collectors.toConcurrentMap(Genre::getId, genre -> genre));
    }
}
