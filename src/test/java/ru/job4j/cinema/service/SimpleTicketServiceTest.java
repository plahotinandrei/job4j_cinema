package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.SessionDetails;
import ru.job4j.cinema.dto.TicketDetails;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.TicketRepository;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleTicketServiceTest {

    private static TicketRepository ticketRepository;

    private static SessionService sessionService;

    private static TicketService ticketService;

    @BeforeAll
    public static void init() throws Exception {
        ticketRepository = mock(TicketRepository.class);
        sessionService = mock(SessionService.class);
        var sessionDetails = SessionDetails.builder()
                .sessionId(1).filmName("Дюна").filmDescription("Описание фильма дюна")
                .filmYear(2021).filmGenre("Комедия").filmMinimalAge(12)
                .fileName("image2.jpg").filePath("files/image2.jpg").hallName("name2").hallDescription("description2")
                .hallRowCount(6).hallPlaceCount(6).startTime("15.06.2024 10:00")
                .filmDurationInMinutes(155).price(600)
                .build();
        when(sessionService.findById(1)).thenReturn(Optional.of(sessionDetails));
        ticketService = new SimpleTicketService(ticketRepository, sessionService);
    }

    @Test
    public void whenSaveTicketForUserThenTicketDetails() {
        var user = new User(1, "Иван Иванов", "ivan@mail.ru", "pass");
        var ticket = new Ticket(1, 1, 5, 5, 1);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(Optional.of(ticket));
        var ticketDetails = TicketDetails.builder()
                .filmName("Дюна").filmGenre("Комедия").filmMinimalAge(12)
                .rowNumber(5).placeNumber(5).hallName("name2")
                .startTime("15.06.2024 10:00").filmDurationInMinutes(155).price(600)
                .build();
        assertThat(ticketService.save(ticket, user)).usingRecursiveComparison().isEqualTo(Optional.of(ticketDetails));
    }

    @Test
    public void whenImpossibleSaveThenEmptyOptional() {
        var user = new User(1, "Иван Иванов", "ivan@mail.ru", "pass");
        var ticket = new Ticket(1, 1, 5, 5, 1);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(Optional.empty());
        when(sessionService.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThat(ticketService.save(ticket, user)).usingRecursiveComparison().isEqualTo(Optional.empty());
    }
}