package ru.kartashov.parser;

import ru.kartashov.model.Task;

import java.util.List;

public interface Parser {
    void writeTasks(List<Task> tasks, String pathToFile);
    void readTasks(List<Task> tasks, String pathToFile);
}
