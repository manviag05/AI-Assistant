package com.example.aiassistant.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RequestState {


    // Set by Orchestrator, BEFORE calling Planner
    private String sessionId;
    private String messageRequest;
    //Planner
    private String intent;
    private boolean isExternalResearch;
    private List<Message> history = new ArrayList<>(); //for storing conversation history
    //SubTask is set by Planner as Task

    //ResearchTool
    private List<Task> subTasks = new ArrayList<>();
    private List<Finding> findings = new ArrayList<>();
    //Finding is set by Research tool

    //Validator
    private boolean isValid;
    private String errorStage;
    private String errorMessage;

    //ResponseWriter
    private String response;

    public void setIsValid(boolean b) {
    }
}
