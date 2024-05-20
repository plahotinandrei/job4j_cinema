package ru.job4j.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.HallRepository;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimpleHallService implements HallService {

    private final HallRepository repository;

    @Override
    public Map<Integer, Hall> findAll() {
        return repository.findAll().stream()
                .collect(Collectors.toConcurrentMap(Hall::getId, hall -> hall));
    }
}
