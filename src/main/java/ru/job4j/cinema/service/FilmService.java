package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmPreview;
import java.util.Map;
import java.util.Optional;

public interface FilmService {

    Optional<FilmPreview> findById(int id);

    Map<Integer, FilmPreview> findAll();
}
