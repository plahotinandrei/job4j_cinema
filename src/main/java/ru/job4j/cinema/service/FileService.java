package ru.job4j.cinema.service;

import ru.job4j.cinema.model.File;
import java.util.Map;

public interface FileService {

    Map<Integer, File> findAll();
}
