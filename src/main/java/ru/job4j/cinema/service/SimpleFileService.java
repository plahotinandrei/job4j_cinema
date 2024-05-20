package ru.job4j.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.repository.FileRepository;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimpleFileService implements FileService {

    private final FileRepository repository;

    @Override
    public Map<Integer, File> findAll() {
        return repository.findAll().stream()
                .collect(Collectors.toConcurrentMap(File::getId, file -> file));
    }
}
