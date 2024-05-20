package ru.job4j.cinema.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SessionDetails {

    private int sessionId;

    private String filmName;

    private String filmDescription;

    private int filmYear;

    private String filmGenre;

    private int filmMinimalAge;

    private String fileName;

    private String filePath;

    private String hallName;

    private String hallDescription;

    private int hallRowCount;

    private int hallPlaceCount;

    private String startTime;

    private int filmDurationInMinutes;

    private int price;
}
