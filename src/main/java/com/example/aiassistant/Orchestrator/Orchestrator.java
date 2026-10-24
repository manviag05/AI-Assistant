package com.example.aiassistant.Orchestrator;

import com.example.aiassistant.Interfaces.PlannerServiceInterface;
import com.example.aiassistant.Interfaces.ResearchServiceInterface;
import com.example.aiassistant.Interfaces.ResponseWriterServiceInterface;
import com.example.aiassistant.Interfaces.ValidatorServiceInterface;
import com.example.aiassistant.dto.ChatRequest;
import com.example.aiassistant.dto.ChatResponse;
import com.example.aiassistant.model.RequestState;
import org.springframework.stereotype.Service;

@Service
public class Orchestrator {

    private final PlannerServiceInterface plannerService;
    private final ResearchServiceInterface researchService;
    private final ResponseWriterServiceInterface responseWriterService;
    private final ValidatorServiceInterface validateService;

    public Orchestrator(PlannerServiceInterface plannerService,
                        ResearchServiceInterface researchService,
                        ResponseWriterServiceInterface responseWriterService,
                        ValidatorServiceInterface validateService) {
        this.plannerService = plannerService;
        this.researchService = researchService;
        this.responseWriterService = responseWriterService;
        this.validateService = validateService;
    }

    public ChatResponse handleChat(ChatRequest chatRequest) {
        RequestState state = new RequestState();
        state.setSessionId(chatRequest.getSessionId());
        state.setMessageRequest(chatRequest.getMessage());

        state = plannerService.plan(state);

        if (state.isExternalResearch()) {
            state = researchService.research(state);
        }

        state = responseWriterService.write(state);
        state = validateService.validate(state);

        if (!state.isValid()) {
            return new ChatResponse(
                    state.getSessionId(),
                    "Sorry, I couldn't provide reliable information for this request. Please try again."
            );
        }

        return new ChatResponse(
                state.getSessionId(), state.getResponse()
        );
    }
}