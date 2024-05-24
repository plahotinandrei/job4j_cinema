package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.HallRepository;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleHallServiceTest {

    private HallRepository hallRepository;

    private HallService hallService;

    @BeforeEach
    public void init() {
        hallRepository = mock(HallRepository.class);
        hallService = new SimpleHallService(hallRepository);
    }

    @Test
    public void whenRepositoryExistThreeHallsThenMapWithThreeHalls() {
        var hall1 = new Hall(1, "name1", 5, 5, "description1");
        var hall2 = new Hall(2, "name2", 6, 6, "description2");
        var hall3 = new Hall(3, "name3", 7, 7, "description3");
        when(hallRepository.findAll()).thenReturn(List.of(hall1, hall2, hall3));
        assertThat(hallService.findAll()).usingRecursiveComparison().isEqualTo(Map.of(1, hall1, 2, hall2, 3, hall3));
    }

    @Test
    public void whenRepositoryNotExistHallsThenEmptyMap() {
        when(hallRepository.findAll()).thenReturn(List.of());
        assertThat(hallService.findAll()).usingRecursiveComparison().isEqualTo(Map.of());
    }
}