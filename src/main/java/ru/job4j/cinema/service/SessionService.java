package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.SessionDetails;
import ru.job4j.cinema.dto.SessionPreview;
import java.util.Collection;
import java.util.Optional;

public interface SessionService {

    Optional<SessionDetails> findById(int id);

    Collection<SessionPreview> findAll();
}
