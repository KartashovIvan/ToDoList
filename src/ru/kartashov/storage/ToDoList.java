package ru.kartashov.storage;

import ru.kartashov.model.Status;
import ru.kartashov.model.Task;
import ru.kartashov.parser.Parser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

public abstract class ToDoList {
    protected final Parser parser;
    protected final String pathToFile;
    protected final List<Task> tasks;

    private static final Logger LOG = Logger.getLogger(ToDoList.class.getName());


    public ToDoList(Parser parser, String pathToFile, List<Task> tasks) {
        this.parser = parser;
        this.pathToFile = pathToFile;
        this.tasks = tasks;
        try {
            FileHandler fileHandler = new FileHandler("ToDoList_log.log", true);
            LOG.addHandler(fileHandler);
            fileHandler.setFormatter(new SimpleFormatter());
            LOG.setUseParentHandlers(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Создание новой задачи
    public String createNewTask(Task task) {
        checkCaption(task.getCaption());
        checkPriority(task.getPriority());
        String id = createID();
        task.setId(id);
        task.setStatus(Status.NEW);
        tasks.add(task);
        parser.writeTasks(tasks, pathToFile);
        LOG.info("Создана задача с id " + id);
        return id;
    }

    //Вывести список всех задач
    public List<Task> printAllTask() {
        tasks.clear();
        parser.readTasks(tasks, pathToFile);
        tasks.forEach(System.out::println);
        LOG.info("Вывод всех задач");
        return tasks;
    }

    //Найти задачи с определенным статусом
    public List<Task> findByStatusTask(String status) {
        tasks.clear();
        parser.readTasks(tasks, pathToFile);
        List<Task> taskByStatus = tasks.stream().filter(task -> task.getStatus().equals(Status.valueOf(status.toUpperCase()))).collect(Collectors.toList());
        taskByStatus.forEach(System.out::println);
        LOG.info("Вывод задач со статусом " + status);
        return taskByStatus;
    }

    //Выполнить задачу
    public void completeTask(String id) {
        Task completeTask = findTaskById(id);
        completeTask.setStatus(Status.DONE);
        parser.writeTasks(tasks, pathToFile);
        LOG.info("Завершено выполнение задачи с id " + id);
    }


    //Удалить задачу
    public void deleteTask(String id) {
        Task deleteTask = findTaskById(id);
        tasks.remove(deleteTask);
        parser.writeTasks(tasks, pathToFile);
        LOG.info("Удалена задача с id " + id);
    }


    //Взять в работу задачу
    public void takeInWork(String id) {
        Task task = findTaskById(id);
        task.setStatus(Status.IN_PROGRESS);
        parser.writeTasks(tasks, pathToFile);
        LOG.info("Взята в работу задача с id " + id);
    }

    //Редактировать задачу
    public void editTask(String id, String caption, String description, String priority, String deadLine) {
        validateTask(findTaskById(id), caption, description, priority, deadLine);
        parser.writeTasks(tasks, pathToFile);
        LOG.info("Завершон процесс редактирования задачи с id " + id);
    }

    public Task findTaskById(String id) {
        return tasks.stream().filter(task -> task.getId().equals(id)).findFirst().orElseThrow(() -> new RuntimeException("Нет задачи с id " + id));
    }

    private void checkPriority(Integer priority) {
        if (priority < 0 || priority > 10) {
            LOG.warning("Указан приоритет не входит в диапазон от 1 до 10");
            throw new RuntimeException("Приоритет задачи может быть от 1 до 10");
        }
    }

    private void validateTask(Task task, String caption, String description, String priority, String deadLine) {
        if (!caption.isBlank()) {
            task.setCaption(caption);
            LOG.info("Изменен заголовок у задачи с id " + task.getId());
        }
        if (!description.isBlank()) {
            task.setDescription(description);
            LOG.info("Изменено описание у задачи с id " + task.getId());
        }
        if (!priority.isBlank()) {
            checkPriority(Integer.parseInt(priority));
            task.setPriority(Integer.parseInt(priority));
            LOG.info("Изменен приоритет у задачи с id " + task.getId());
        }
        if (!deadLine.isBlank()) {
            task.setDeadLine(LocalDate.parse(deadLine));
            LOG.info("Изменен срок у задачи с id " + task.getId());
        }
    }

    private void checkCaption(String caption) {
        if(caption.length() > 50) {
            LOG.warning("Длина заголовка не должна быть больше 50 символов");
            throw new RuntimeException("Длина заголовка больше 50 символов");
        }
    }

    //Создать Id задаче
    protected abstract String createID();
}
