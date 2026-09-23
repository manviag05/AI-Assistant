package com.example.aiassistant.Orchestrator;

import com.example.aiassistant.Service.PlannerServiceInterfaceImpl;
import com.example.aiassistant.Service.ResearchServiceInterfaceImpl;
import com.example.aiassistant.dto.ChatRequest;
import com.example.aiassistant.dto.ChatResponse;
import com.example.aiassistant.model.RequestState;
import org.springframework.stereotype.Service;

@Service
public class Orchestrator {
    //So orchestrator will give requestState its values, it will call planner service get the details from it and set it using setters in requestState
    private final PlannerServiceInterfaceImpl plannerServiceInterfaceImpl;
    private final ResearchServiceInterfaceImpl researchServiceInterfaceImpl;

    public Orchestrator(PlannerServiceInterfaceImpl plannerServiceInterfaceImpl, ResearchServiceInterfaceImpl researchServiceInterfaceImpl){
        this.plannerServiceInterfaceImpl = plannerServiceInterfaceImpl;
        this.researchServiceInterfaceImpl = researchServiceInterfaceImpl;
    }

    public ChatResponse handleChat(ChatRequest chatRequest){
        RequestState state = new RequestState();
        state.setSessionId(chatRequest.getSessionId());
        state.setMessageRequest(chatRequest.getMessage());

        state = plannerServiceInterfaceImpl.plan(state);

        if(state.isExternalResearch()){
            state = researchServiceInterfaceImpl.research(state);
        }

        return new ChatResponse(
                state.getSessionId(),
                "Intent: " + state.getIntent()
                        + ", researchNeeded: " + state.isExternalResearch()
                        + ", subTasks: " + state.getSubTasks()
                        + " | findings: " + state.getFindings()
        );
    }

}
