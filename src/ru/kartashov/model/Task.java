package ru.kartashov.model;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private String id;
    private String caption;
    private String description;
    private Integer priority;
    private LocalDate deadLine;
    private Status status;
    private LocalDate complete;

    public Task() {
    }

    public Task(String id, String caption, String description, Integer priority, LocalDate deadLine, Status status, LocalDate complete) {
        this.id = id;
        this.caption = caption;
        this.description = description;
        this.priority = priority;
        this.deadLine = deadLine;
        this.status = status;
        this.complete = complete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getComplete() {
        return complete;
    }

    public void setComplete(LocalDate complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("id: " + id).append(", caption: " + caption).append(", description: " + description)
                .append(", priority: " + priority).append(", deadLine: " + deadLine).append(", status: " + status);
        if (Objects.isNull(complete)){
            return sb.toString();
        }
        return sb.append(", complete: " + complete).toString();
    }

}
