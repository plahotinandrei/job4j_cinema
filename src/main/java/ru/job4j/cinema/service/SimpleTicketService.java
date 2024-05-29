package ru.job4j.cinema.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.TicketDetails;
import ru.job4j.cinema.mapper.TicketMapper;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.TicketRepository;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;

    private final SessionService sessionService;

    private final TicketMapper ticketMapper = Mappers.getMapper(TicketMapper.class);

    @Override
    public Optional<TicketDetails> save(Ticket ticket, User user) {
        var ticketOptional = ticketRepository.save(ticket);
        if (ticketOptional.isPresent()) {
            var sessionDetails = sessionService.findById(ticket.getSessionId()).get();
            return Optional.of(ticketMapper.getTicketDetails(ticket, sessionDetails));
        }
        return Optional.empty();
    }
}
