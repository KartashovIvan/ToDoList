package ru.kartashov.storage;

import ru.kartashov.model.Task;
import ru.kartashov.parser.Parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class XMLToDoList extends ToDoList {
    private static Integer ID_TASK_COUNTER = 0;
    public XMLToDoList(Parser parser, String pathToFile) {
        super(parser, pathToFile, new ArrayList<>());
        File database = new File(pathToFile);
        if (database.exists()) {
            parser.readTasks(tasks, pathToFile);
            ID_TASK_COUNTER = Integer.parseInt(tasks.stream().max(Comparator.comparing(Task::getId)).orElseThrow().getId());
        }
    }

    @Override
    public String createID() {
        return (++ID_TASK_COUNTER).toString();
    }
}
