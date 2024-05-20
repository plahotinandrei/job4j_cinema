package ru.job4j.cinema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/buy")
    public String createTicket(@ModelAttribute Ticket ticket, Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        var ticketDetailsOptional = ticketService.save(ticket, user);
        if (ticketDetailsOptional.isEmpty()) {
            model.addAttribute("message", "Не удалось приобрести билет на заданное место. Вероятно оно уже занято. Перейдите на страницу бронирования билетов и попробуйте снова.");
            return "errors/404";
        }
        model.addAttribute("ticketDetails", ticketDetailsOptional.get());
        return "tickets/success";
    }
}
