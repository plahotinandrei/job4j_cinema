package ru.job4j.cinema.model;

import lombok.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {

    private int id;

    private String name;

    private String description;

    private int year;

    private Genre genre;

    private int minimalAge;

    private int durationInMinutes;

    private File file;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Film film = (Film) o;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
