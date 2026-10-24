package com.example.aiassistant.Service;

import com.example.aiassistant.Interfaces.ValidatorServiceInterface;
import com.example.aiassistant.model.Finding;
import com.example.aiassistant.model.RequestState;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//understand this code
@Service
public class ValidatorServiceInterfaceImpl implements ValidatorServiceInterface {
    private final FactCheckingEvaluator factCheckingEvaluator;
    private final RelevancyEvaluator relevancyEvaluator;

    public ValidatorServiceInterfaceImpl(ChatClient.Builder chatClientBuilder) {
        this.factCheckingEvaluator = FactCheckingEvaluator.builder(chatClientBuilder).build();
        this.relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);
    }

    @Override
    public RequestState validate(RequestState state) {

        if (state.getResponse() == null || state.getResponse().isBlank()) {
            state.setIsValid(false);
            state.setErrorStage("Writer");
            state.setErrorMessage("Writer produced an empty response.");
            return state;
        }

        if (state.getFindings() == null || state.getFindings().isEmpty()) {
            state.setIsValid(true);
            return state;
        }

        List<Document> context = buildContextDocuments(state.getFindings());

        EvaluationRequest evaluationRequest = new EvaluationRequest(
                state.getMessageRequest(),
                context,
                state.getResponse()
        );

        EvaluationResponse factCheckResult = factCheckingEvaluator.evaluate(evaluationRequest);
        EvaluationResponse relevancyResult = relevancyEvaluator.evaluate(evaluationRequest);

        if (factCheckResult.isPass() && relevancyResult.isPass()) {
            state.setIsValid(true);
        } else {
            state.setIsValid(false);
            state.setErrorStage("Validator");
            state.setErrorMessage(buildFailureReason(factCheckResult, relevancyResult));
        }

        return state;
    }

    private String buildFailureReason(EvaluationResponse factCheck, EvaluationResponse relevancy) {
        if (!factCheck.isPass() && !relevancy.isPass()) {
            return "Response failed both fact-checking and relevancy checks.";
        } else if (!factCheck.isPass()) {
            return "Response failed fact-checking against research findings.";
        } else {
            return "Response failed relevancy check against the original query.";
        }
    }

    private List<Document> buildContextDocuments(List<Finding> findings) {
        List<Document> documents = new ArrayList<>();
        for (Finding finding : findings) {
            documents.add(new Document(finding.getResearchResponse()));
        }
        return documents;
    }
}
