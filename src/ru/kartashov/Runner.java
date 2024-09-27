package ru.kartashov;

import ru.kartashov.model.Task;
import ru.kartashov.parser.XMLParser;
import ru.kartashov.storage.ToDoList;
import ru.kartashov.storage.XMLToDoList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

public class Runner {
    private final static ToDoList TO_DO_LIST = new XMLToDoList(new XMLParser(), "./ToDoList.xml");

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("Введите команду \n");
            String[] param = reader.readLine().trim().toLowerCase().split(" ");
            Task task = null;

            switch (param[0]) {
                case "help":
                    System.out.println("""
                            Создание новой задачи:
                                new
                            Выполнить задачу:
                                complete id-задачи
                            Удалить задачу:
                                delete id-задачи
                            Вывод задач:
                                всех: list
                                новых: list -s new
                                в работе: list -s in_progress
                                выполненных: list -s done
                            Взять в работу задачу:
                                take id-задачи
                            Редактировать задачу:
                                edit id-задачи
                            Выход:
                                exit
                            """);
                    break;
                case "new":
                    task = new Task();
                    System.out.print("Введите заголовок (Не больше 50 символов): ");
                    task.setCaption(reader.readLine());
                    System.out.print("Введите описание: ");
                    task.setDescription(reader.readLine());
                    System.out.print("Введите важность (от 1 до 10): ");
                    task.setPriority(Integer.parseInt(reader.readLine()));
                    System.out.print("Введите срок (Формат ГГГГ-ММ-ЧЧ): ");
                    task.setDeadLine(LocalDate.parse(reader.readLine()));

                    TO_DO_LIST.createNewTask(task);
                    break;
                case "complete":
                    if (param.length == 1) {
                        System.out.println("Неверная команда.");
                        continue;
                    }
                    TO_DO_LIST.completeTask(param[1]);
                    break;
                case "delete":
                    if (param.length == 1) {
                        System.out.println("Неверная команда.");
                        continue;
                    }
                    TO_DO_LIST.deleteTask(param[1]);
                    break;
                case "list":
                    if (param.length == 1) {
                        TO_DO_LIST.printAllTask();
                    } else if (param[1].equals("-s")) {
                        if (param.length == 2) {
                            System.out.println("Неверная команда.");
                            continue;
                        }
                        TO_DO_LIST.findByStatusTask(param[2]);
                    }
                    break;
                case "take":
                    if (param.length == 1) {
                        System.out.println("Неверная команда.");
                        continue;
                    }
                    TO_DO_LIST.takeInWork(param[1]);
                    break;
                case "edit":
                    if (param.length == 1) {
                        System.out.println("Неверная команда.");
                        continue;
                    }
                    System.out.println("Если не нужно менять пункт, просто нажминет 'Enter'");
                    System.out.print("Введите заголовок: ");
                    String caption = reader.readLine();
                    System.out.print("Введите описание: ");
                    String description = reader.readLine();
                    System.out.print("Введите важность (от 1 до 10): ");
                    String priority = reader.readLine();
                    System.out.print("Введите срок (Формат ГГГГ-ММ-ЧЧ): ");
                    String deadLine = reader.readLine();

                    TO_DO_LIST.editTask(param[1], caption, description, priority, deadLine);
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("Неверная команда.");
                    break;
            }
        }
    }
}
