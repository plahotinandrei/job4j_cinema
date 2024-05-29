package ru.job4j.cinema.model;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString
public class User {

    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "full_name", "name",
            "email", "email",
            "password", "password"
    );

    private int id;

    private String name;

    private String email;

    private String password;
}
