package ru.job4j.cinema.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FilmPreview {

    private String name;

    private String description;

    private int year;

    private String genre;

    private int minimalAge;

    private int durationInMinutes;

    private String fileName;

    private String filePath;
}
