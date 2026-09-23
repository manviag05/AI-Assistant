package com.example.aiassistant.Interfaces;

import com.example.aiassistant.model.RequestState;
import org.springframework.stereotype.Service;

public interface ResearchServiceInterface {
    RequestState research(RequestState state);
}
