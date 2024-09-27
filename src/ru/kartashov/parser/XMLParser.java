package ru.kartashov.parser;

import ru.kartashov.model.Status;
import ru.kartashov.model.Task;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class XMLParser implements Parser{
    private final DocumentBuilder documentBuilder;
    private final Transformer transformer;
    public XMLParser() {
        try {
            this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            this.transformer = TransformerFactory.newInstance().newTransformer();
        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    //    Запись в файл с задачами
    public void writeTasks(List<Task> tasks, String pathToFile) {
        Document document = documentBuilder.newDocument();
        Element root = mapToDoList(document, tasks);
        document.appendChild(root);
        try {
            transformer.transform(new DOMSource(document), new StreamResult(new File(pathToFile)));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    //    Чтение файла с задачами
    public void readTasks(List<Task> tasks, String pathToFile) {
        Document parseDocument;
        try {
            parseDocument = documentBuilder.parse(pathToFile);
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
        Element root = parseDocument.getDocumentElement();
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Task createTask = new Task();
            Node task = childNodes.item(i);
            mapAttributesTask(task, createTask);
            mapInfoTask(task.getChildNodes(), createTask);
            tasks.add(createTask);
        }
    }

    //    Создание структуры файла с задачами
    private Element mapToDoList(Document document, List<Task> tasks) {
        Element root = document.createElement("ToDoList");
        for (Task task : tasks) {
            root.appendChild(mapTask(document, task));
        }
        return root;
    }

    //   Мапинг обьекта Task в xml файл
    private Element mapTask(Document document, Task task) {
        Element newTask = document.createElement("Task");
        newTask.setAttribute("id", task.getId());
        newTask.setAttribute("caption", task.getCaption());

        Element description = document.createElement("Description");
        description.setTextContent(task.getDescription());

        Element priority = document.createElement("Priority");
        priority.setTextContent(task.getPriority().toString());

        Element deadline = document.createElement("Deadline");
        deadline.setTextContent(task.getDeadLine().toString());

        Element status = document.createElement("Status");
        status.setTextContent(task.getStatus().toString());

        newTask.appendChild(description);
        newTask.appendChild(priority);
        newTask.appendChild(deadline);
        newTask.appendChild(status);

        Status statusTask = task.getStatus();

        if (statusTask.equals(Status.DONE) || Objects.nonNull(task.getComplete())) {
            Element complete = document.createElement("Complete");
            if (Objects.nonNull(task.getComplete())) {
                complete.setTextContent(task.getComplete().toString());
            } else if (statusTask.equals(Status.DONE)) {
                complete.setTextContent(LocalDate.now().toString());
            }
            newTask.appendChild(complete);
        }

        return newTask;
    }

    //    Мапинг атрибутов тега "Task" к обьекту Task
    private void mapAttributesTask(Node task, Task createTask) {
        NamedNodeMap attributes = task.getAttributes();
        createTask.setId(attributes.getNamedItem("id").getTextContent());
        createTask.setCaption(attributes.getNamedItem("caption").getTextContent());
    }

    //    Мапинг описание информации о задачи в обьек Task
    private void mapInfoTask(NodeList taskInfo, Task createTask) {
        for (int j = 0; j < taskInfo.getLength(); j++) {
            Node info = taskInfo.item(j);
            String nodeName = info.getNodeName();
            if (nodeName.equals("Description")) {
                createTask.setDescription(info.getTextContent());
            }
            if (nodeName.equals("Priority")) {
                createTask.setPriority(Integer.parseInt(info.getTextContent()));
            }
            if (nodeName.equals("Deadline")) {
                createTask.setDeadLine(LocalDate.parse(info.getTextContent()));
            }
            if (nodeName.equals("Status")) {
                createTask.setStatus(Status.valueOf(info.getTextContent()));
            }
            if (nodeName.equals("Complete")) {
                createTask.setComplete(LocalDate.parse(info.getTextContent()));
            }
        }
    }
}
