package ru.job4j.cinema.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.cinema.dto.FilmPreview;
import ru.job4j.cinema.dto.SessionDetails;
import ru.job4j.cinema.dto.SessionPreview;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Session;

@Mapper
public interface SessionMapper {

    @Mapping(source = "session.id", target = "id")
    @Mapping(source = "session.startTime", target = "startTime", dateFormat = "dd.MM.yyyy HH:mm")
    @Mapping(source = "filmPreview.name", target = "filmName")
    @Mapping(source = "filmPreview.genre", target = "filmGenre")
    @Mapping(source = "filmPreview.minimalAge", target = "filmMinimalAge")
    @Mapping(source = "filmPreview.durationInMinutes", target = "filmDurationInMinutes")
    @Mapping(source = "hall.name", target = "hallName")
    SessionPreview getSessionPreview(Session session, FilmPreview filmPreview, Hall hall);

    @Mapping(source = "session.id", target = "id")
    @Mapping(source = "session.startTime", target = "startTime", dateFormat = "dd.MM.yyyy HH:mm")
    @Mapping(source = "filmPreview.name", target = "filmName")
    @Mapping(source = "filmPreview.description", target = "filmDescription")
    @Mapping(source = "filmPreview.year", target = "filmYear")
    @Mapping(source = "filmPreview.genre", target = "filmGenre")
    @Mapping(source = "filmPreview.minimalAge", target = "filmMinimalAge")
    @Mapping(source = "filmPreview.durationInMinutes", target = "filmDurationInMinutes")
    @Mapping(source = "hall.name", target = "hallName")
    @Mapping(source = "hall.description", target = "hallDescription")
    @Mapping(source = "hall.rowCount", target = "hallRowCount")
    @Mapping(source = "hall.placeCount", target = "hallPlaceCount")
    SessionDetails getSessionDetails(Session session, FilmPreview filmPreview, Hall hall);
}
