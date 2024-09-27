package ru.kartashov.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kartashov.model.Status;
import ru.kartashov.model.Task;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ToDoListTest {
    private final ToDoList toDoList ;
    private static final Task NEW_TEST_TASK_1 = new Task(null,
            "test_caption_1",
            "test_description_1",
            5,
            LocalDate.of(2020, 10, 10),
            null,
            null);
    private static final Task NEW_TEST_TASK_2 = new Task(null,
            "test_caption_2",
            "test_description_2",
            6,
            LocalDate.of(2021, 11, 13),
            null,
            null);
    private static final Task NEW_TEST_TASK_3 = new Task(null,
            "test_caption_3",
            "test_description_3",
            7,
            LocalDate.of(2022, 12, 14),
            null,
            null);

    private static final Task NEW_TEST_TASK_4 = new Task(null,
            "заголовок",
            "описание",
            5,
            LocalDate.of(2024, 10, 10),
            null,
            null);

    public ToDoListTest(ToDoList toDoList) {
        this.toDoList = toDoList;
    }

    @BeforeEach
    void beforeTest() {
        toDoList.tasks.clear();
        toDoList.parser.writeTasks(toDoList.tasks, toDoList.pathToFile);
        toDoList.createNewTask(NEW_TEST_TASK_1);
        toDoList.createNewTask(NEW_TEST_TASK_2);
        toDoList.createNewTask(NEW_TEST_TASK_3);
    }

    @Test
    void createNewTask_success() {
        String id = createTestTask();
        Task task = toDoList.findTaskById(id);
        assertEquals(4, toDoList.tasks.size());
        assertEquals(id, task.getId());
        assertEquals("заголовок", task.getCaption());
        assertEquals("описание", task.getDescription());
        assertEquals(5, task.getPriority());
        assertEquals(LocalDate.of(2024, 10, 10), task.getDeadLine());
        assertEquals(task.getStatus(), Status.NEW);
    }

    @Test
    void findByStatusTask() {
        String id = createTestTask();
        toDoList.takeInWork(id);
        List<Task> taskByStatus = toDoList.findByStatusTask("IN_PROGRESS");
        assertEquals(1, taskByStatus.size());
    }

    @Test
    void completeTask() {
        String id = createTestTask();
        toDoList.completeTask(id);
        toDoList.tasks.clear();
        toDoList.parser.readTasks(toDoList.tasks, toDoList.pathToFile);
        Task task = toDoList.findTaskById(id);
        assertEquals(task.getStatus(), Status.DONE);
        assertNotNull(task.getComplete());
    }

    @Test
    void deleteTask() {
        String id = createTestTask();
        assertEquals(4, toDoList.tasks.size());
        toDoList.deleteTask(id);
        assertEquals(3, toDoList.tasks.size());
    }

    @Test
    void takeInWork() {
        String id = createTestTask();
        toDoList.takeInWork(id);
        toDoList.tasks.clear();
        toDoList.parser.readTasks(toDoList.tasks, toDoList.pathToFile);
        Task task = toDoList.findTaskById(id);
        assertEquals(task.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void editTask() {
        String id = createTestTask();
        toDoList.editTask(id,"другой заголовок","другое описание", "10",
                "2022-11-15");
        Task task = toDoList.findTaskById(id);
        assertEquals(id, task.getId());
        assertEquals("другой заголовок", task.getCaption());
        assertEquals("другое описание", task.getDescription());
        assertEquals(10, task.getPriority());
        assertEquals(LocalDate.of(2022, 11, 15), task.getDeadLine());

    }

    private String createTestTask() {
        return toDoList.createNewTask(NEW_TEST_TASK_4);
    }
}