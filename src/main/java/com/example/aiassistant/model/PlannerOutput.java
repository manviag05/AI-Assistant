package com.example.aiassistant.model;


import java.util.List;

public record PlannerOutput(
        String intent,
        Boolean isExternalResearch,
        List<Task> subTasks
) {
}
