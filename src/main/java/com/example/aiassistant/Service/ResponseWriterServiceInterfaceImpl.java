package com.example.aiassistant.Service;

import com.example.aiassistant.Interfaces.LlmServiceInterface;
import com.example.aiassistant.Interfaces.ResponseWriterServiceInterface;
import com.example.aiassistant.model.Finding;
import com.example.aiassistant.model.RequestState;
import org.springframework.stereotype.Service;

@Service
public class ResponseWriterServiceInterfaceImpl implements ResponseWriterServiceInterface {


    private final LlmServiceInterface llmService;

    public ResponseWriterServiceInterfaceImpl(LlmServiceInterface llmService) {
        this.llmService = llmService;
    }

    @Override
    public RequestState write(RequestState state) {
        String prompt = buildPrompt(state);
        String response = llmService.call(prompt);
        state.setResponse(response);
        return state;
    }

    private String buildPrompt(RequestState state){
        StringBuilder findingsBlock = new StringBuilder();

        if(state.getFindings()!=null && !state.getFindings().isEmpty()){
            findingsBlock.append("Here is relevant research to base your answer on:\n");
            for(Finding finding: state.getFindings()){
                findingsBlock.append("- Topic: ").append(finding.getTopic())
                        .append("\n  Info: ").append(finding.getResearchResponse())
                        .append("\n  Source: ").append(finding.getSourceUrl())
                        .append("\n");
            }
        }
        else {
            findingsBlock.append("No external research was needed or available for this query. Answer using your own knowledge.");
        }

        return """
            You are a helpful assistant answering a user's question.

            User question: %s
            Question type: %s

            %s

            Instructions:
            - Answer the question clearly and directly.
            - If research findings are provided above, base your answer on them and cite sources where relevant.
            - If no research was provided, answer from your own knowledge, and if the question is time-sensitive, note that your information may not be current.
            - Do not fabricate information not supported by the given research.
            """.formatted(
                state.getMessageRequest(),
                state.getIntent(),
                findingsBlock.toString()
        );
    }
}
