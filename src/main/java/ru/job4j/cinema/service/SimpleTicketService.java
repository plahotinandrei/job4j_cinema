package ru.job4j.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.TicketDetails;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.TicketRepository;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;

    private final SessionService sessionService;

    @Override
    public Optional<TicketDetails> save(Ticket ticket, User user) {
        var ticketOptional = ticketRepository.save(ticket);
        if (ticketOptional.isPresent()) {
            var sessionDetails = sessionService.findById(ticket.getSessionId()).get();
            return Optional.of(TicketDetails.builder()
                            .filmName(sessionDetails.getFilmName())
                            .filmGenre(sessionDetails.getFilmGenre())
                            .filmMinimalAge(sessionDetails.getFilmMinimalAge())
                            .rowNumber(ticket.getRowNumber())
                            .placeNumber(ticket.getPlaceNumber())
                            .hallName(sessionDetails.getHallName())
                            .startTime(sessionDetails.getStartTime())
                            .filmDurationInMinutes(sessionDetails.getFilmDurationInMinutes())
                            .price(sessionDetails.getPrice())
                    .build());
        }
        return Optional.empty();
    }
}
