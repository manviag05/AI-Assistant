package com.example.aiassistant.controller;

import com.example.aiassistant.Orchestrator.Orchestrator;
import com.example.aiassistant.dto.ChatRequest;
import com.example.aiassistant.dto.ChatResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enter")
public class ChatController {
    //Basically I want a /postquery api here and this controller should act as an entry point to the backend logic of the ai-assistant

    private final Orchestrator orchestrator;

    public ChatController(Orchestrator orchestrator){
        this.orchestrator = orchestrator;
    }

    @PostMapping("/query")
    public ChatResponse PostQuery(@Valid @RequestBody ChatRequest request){
//        call to orchestrator that manages underlying services
//        Planner service -> Research tool service -> Validator Service -> ResponseGenerator Service -> Response to UI
        return orchestrator.handleChat(request);
    }
}
