package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.FilmPreview;
import ru.job4j.cinema.dto.SessionDetails;
import ru.job4j.cinema.dto.SessionPreview;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.HallRepository;
import ru.job4j.cinema.repository.SessionRepository;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleSessionServiceTest {

    private static SessionRepository sessionRepository;

    private static FilmService filmService;

    private static HallRepository hallRepository;

    private static HallService hallService;

    private static SessionService sessionService;

    @BeforeAll
    public static void init() throws Exception {
        sessionRepository = mock(SessionRepository.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        var session1 = new Session(1, 1, 1,
                new Timestamp(dateFormat.parse("2024-06-15 8:00:00").getTime()),
                new Timestamp(dateFormat.parse("2024-06-15 10:00:00").getTime()), 500);
        var session2 = new Session(2, 2, 2,
                new Timestamp(dateFormat.parse("2024-06-15 10:00:00").getTime()),
                new Timestamp(dateFormat.parse("2024-06-15 12:00:00").getTime()), 600);
        var session3 = new Session(3, 3, 3,
                new Timestamp(dateFormat.parse("2024-06-15 12:00:00").getTime()),
                new Timestamp(dateFormat.parse("2024-06-15 14:00:00").getTime()), 700);
        when(sessionRepository.findAll()).thenReturn(List.of(session1, session2, session3));
        when(sessionRepository.findById(1)).thenReturn(Optional.of(session1));
        when(sessionRepository.findById(2)).thenReturn(Optional.of(session2));
        when(sessionRepository.findById(3)).thenReturn(Optional.of(session3));
        filmService = mock(FilmService.class);
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
        when(filmService.findAll()).thenReturn(Map.of(1, filmPreview1, 2, filmPreview2, 3, filmPreview3));
        when(filmService.findById(1)).thenReturn(Optional.of(filmPreview1));
        when(filmService.findById(2)).thenReturn(Optional.of(filmPreview2));
        when(filmService.findById(3)).thenReturn(Optional.of(filmPreview3));
        hallRepository = mock(HallRepository.class);
        var hall1 = new Hall(1, "name1", 5, 5, "description1");
        var hall2 = new Hall(2, "name2", 6, 6, "description2");
        var hall3 = new Hall(3, "name3", 7, 7, "description3");
        when(hallRepository.findAll()).thenReturn(List.of(hall1, hall2, hall3));
        when(hallRepository.findById(1)).thenReturn(Optional.of(hall1));
        when(hallRepository.findById(2)).thenReturn(Optional.of(hall2));
        when(hallRepository.findById(3)).thenReturn(Optional.of(hall3));
        hallService = mock(HallService.class);
        when(hallService.findAll()).thenReturn(Map.of(1, hall1, 2, hall2, 3, hall3));
        sessionService = new SimpleSessionService(sessionRepository, filmService, hallRepository, hallService);
    }

    @Test
    public void whenRepositoryExistThreeSessionsThenMapWithThreeSessions() {
        var sessionPreview1 = SessionPreview.builder()
                .id(1).filmName("Аватар").filmGenre("Боевик").filmMinimalAge(12)
                .hallName("name1").startTime("15.06.2024 08:00").filmDurationInMinutes(179).price(500)
                .build();
        var sessionPreview2 = SessionPreview.builder()
                .id(2).filmName("Дюна").filmGenre("Комедия").filmMinimalAge(12)
                .hallName("name2").startTime("15.06.2024 10:00").filmDurationInMinutes(155).price(600)
                .build();
        var sessionPreview3 = SessionPreview.builder()
                .id(3).filmName("Грань будущего").filmGenre("Драма").filmMinimalAge(12)
                .hallName("name3").startTime("15.06.2024 12:00").filmDurationInMinutes(113).price(700)
                .build();
        assertThat(sessionService.findAll()).usingRecursiveComparison()
                .isEqualTo(List.of(sessionPreview1, sessionPreview2, sessionPreview3));
    }

    @Test
    public void whenFindByIdThenSessionPreview() {
        var sessionDetails = SessionDetails.builder()
                .sessionId(2).filmName("Дюна").filmDescription("Описание фильма дюна")
                .filmYear(2021).filmGenre("Комедия").filmMinimalAge(12)
                .fileName("image2.jpg").filePath("files/image2.jpg").hallName("name2").hallDescription("description2")
                .hallRowCount(6).hallPlaceCount(6).startTime("15.06.2024 10:00")
                .filmDurationInMinutes(155).price(600)
                .build();
        assertThat(sessionService.findById(2)).usingRecursiveComparison().isEqualTo(Optional.of(sessionDetails));
    }

    @Test
    public void whenNotExistSessionThenEmptyOptional() {
        assertThat(sessionService.findById(4)).isEqualTo(Optional.empty());
    }
}