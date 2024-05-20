package ru.job4j.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmPreview;
import ru.job4j.cinema.dto.SessionDetails;
import ru.job4j.cinema.dto.SessionPreview;
import ru.job4j.cinema.model.Hall;
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

    @Override
    public Optional<SessionDetails> findById(int id) {
        Optional<Session> sessionOptional = sessionRepository.findById(id);
        if (sessionOptional.isPresent()) {
            var session = sessionOptional.get();
            var film = filmService.findById(session.getFilmId()).get();
            var hall = hallRepository.findById(session.getHallId()).get();
            return Optional.of(SessionDetails.builder()
                            .sessionId(session.getId())
                            .filmName(film.getName())
                            .filmDescription(film.getDescription())
                            .filmYear(film.getYear())
                            .filmGenre(film.getGenre())
                            .filmMinimalAge(film.getMinimalAge())
                            .fileName(film.getFileName())
                            .filePath(film.getFilePath())
                            .hallName(hall.getName())
                            .hallDescription(hall.getDescription())
                            .hallRowCount(hall.getRowCount())
                            .hallPlaceCount(hall.getPlaceCount())
                            .startTime(dateFormatter(session.getStartTime()))
                            .filmDurationInMinutes(film.getDurationInMinutes())
                            .price(session.getPrice())
                    .build());
        }
        return Optional.empty();
    }

    @Override
    public Collection<SessionPreview> findAll() {
        var films = filmService.findAll();
        var halls = hallService.findAll();
        return sessionRepository.findAll().stream()
                .map((session) -> SessionPreview.builder()
                        .id(session.getId())
                        .filmName(films.get(session.getFilmId()).getName())
                        .filmGenre(films.get(session.getFilmId()).getGenre())
                        .filmMinimalAge(films.get(session.getFilmId()).getMinimalAge())
                        .hallName(halls.get(session.getHallId()).getName())
                        .startTime(dateFormatter(session.getStartTime()))
                        .filmDurationInMinutes(films.get(session.getFilmId()).getDurationInMinutes())
                        .price(session.getPrice())
                        .build())
                .toList();
    }

    private String dateFormatter(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return sdf.format(timestamp);
    }
}
