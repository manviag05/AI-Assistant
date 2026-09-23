package com.example.aiassistant.Interfaces;

import com.example.aiassistant.Service.LlmServiceInterfaceImpl;
import com.example.aiassistant.model.RequestState;
import org.springframework.stereotype.Service;

public interface PlannerServiceInterface {
    RequestState plan(RequestState state);
}
