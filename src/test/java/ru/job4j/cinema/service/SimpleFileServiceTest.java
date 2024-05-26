package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.repository.FileRepository;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFileServiceTest {

    private static FileRepository fileRepository;

    private static FileService fileService;

    @BeforeAll
    public static void init() {
        fileRepository = mock(FileRepository.class);
        fileService = new SimpleFileService(fileRepository);
    }

    @Test
    public void whenRepositoryExistThreeFilesThenMapWithThreeFiles() {
        var file1 = new File(1, "image1.jpg", "files/image1.jpg");
        var file2 = new File(2, "image2.jpg", "files/image2.jpg");
        var file3 = new File(3, "image3.jpg", "files/image3.jpg");
        when(fileRepository.findAll()).thenReturn(List.of(file1, file2, file3));
        assertThat(fileService.findAll()).usingRecursiveComparison().isEqualTo(Map.of(1, file1, 2, file2, 3, file3));
    }

    @Test
    public void whenRepositoryNotExistFilesThenEmptyMap() {
        when(fileRepository.findAll()).thenReturn(List.of());
        assertThat(fileService.findAll()).usingRecursiveComparison().isEqualTo(Map.of());
    }
}