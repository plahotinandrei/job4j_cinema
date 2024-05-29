package ru.job4j.cinema.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class File {

    private int id;

    private String name;

    private String path;
}
