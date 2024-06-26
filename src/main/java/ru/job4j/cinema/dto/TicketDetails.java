package ru.job4j.cinema.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TicketDetails {

    private String filmName;

    private String filmGenre;

    private int filmMinimalAge;

    private int rowNumber;

    private int placeNumber;

    private String hallName;

    private String startTime;

    private int filmDurationInMinutes;

    private int price;
}
