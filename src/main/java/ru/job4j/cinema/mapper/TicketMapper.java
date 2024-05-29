package ru.job4j.cinema.mapper;

import org.mapstruct.Mapper;
import ru.job4j.cinema.dto.SessionDetails;
import ru.job4j.cinema.dto.TicketDetails;
import ru.job4j.cinema.model.Ticket;

@Mapper
public interface TicketMapper {

    TicketDetails getTicketDetails(Ticket ticket, SessionDetails sessionDetails);
}
