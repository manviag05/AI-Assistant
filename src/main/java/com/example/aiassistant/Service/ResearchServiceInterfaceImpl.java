package com.example.aiassistant.Service;

import com.example.aiassistant.Interfaces.ResearchServiceInterface;
import com.example.aiassistant.model.Finding;
import com.example.aiassistant.model.RequestState;
import com.example.aiassistant.model.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ResearchServiceInterfaceImpl implements ResearchServiceInterface {

    private final RestClient restClient;

    @Value("${tavily.api-key}")
    private String api_key;

    public ResearchServiceInterfaceImpl(RestClient.Builder restClientBuilder){
        this.restClient = restClientBuilder
                .baseUrl("https://api.tavily.com")
                .build();
    }

    @Override
    public RequestState research(RequestState state){
        List<Finding> findings = new ArrayList<>();

        for(Task task: state.getSubTasks()){
            Finding finding = searchOneTask(task);
            findings.add(finding);
        }

        state.setFindings(findings);

        return state;
    }

    private Finding searchOneTask(Task task){

        //first we will make requestBody
        Map<String, Object> requestBody = Map.of(
                "api_key", api_key,
                "query", task.getSubTaskMessage(),
                "maxresults", 3
        );

        Map<String, Object> response = restClient.post()
                .uri("/search")
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

        if (results == null || results.isEmpty()) {
            return new Finding(task.getTopic(), "No results found.", null);
        }

        Map<String, Object> topResult = results.get(0);
        String summary = (String)topResult.get("content");
        String sourceUrl = (String)topResult.get("url");

        return new Finding(task.getTopic(), summary, sourceUrl);

    }

}
