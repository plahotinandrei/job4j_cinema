package ru.job4j.cinema.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SessionPreview {

    private int id;

    private String filmName;

    private String filmGenre;

    private int filmMinimalAge;

    private String hallName;

    private String startTime;

    private int filmDurationInMinutes;

    private int price;
}
