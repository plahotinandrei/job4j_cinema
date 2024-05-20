package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.TicketDetails;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import java.util.Optional;

public interface TicketService {

    Optional<TicketDetails> save(Ticket ticket, User user);
}
