package ru.job4j.cinema.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.SessionDetails;
import ru.job4j.cinema.dto.SessionPreview;
import ru.job4j.cinema.mapper.SessionMapper;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.HallRepository;
import ru.job4j.cinema.repository.SessionRepository;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleSessionService implements SessionService {

    private final SessionRepository sessionRepository;

    private final FilmService filmService;

    private final HallRepository hallRepository;

    private final HallService hallService;

    private final SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    @Override
    public Optional<SessionDetails> findById(int id) {
        Optional<Session> sessionOptional = sessionRepository.findById(id);
        if (sessionOptional.isPresent()) {
            var session = sessionOptional.get();
            var filmOptional = filmService.findById(session.getFilmId());
            var hallOptional = hallRepository.findById(session.getHallId());
            if (filmOptional.isPresent() && hallOptional.isPresent()) {
                var film = filmOptional.get();
                var hall = hallOptional.get();
                return Optional.of(sessionMapper.getSessionDetails(session, film, hall));
            }
        }
        return Optional.empty();
    }

    @Override
    public Collection<SessionPreview> findAll() {
        var films = filmService.findAll();
        var halls = hallService.findAll();
        return sessionRepository.findAll().stream()
                .map((session) -> sessionMapper
                        .getSessionPreview(session, films.get(session.getFilmId()), halls.get(session.getHallId())))
                .toList();
    }
}
