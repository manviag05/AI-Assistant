package com.example.aiassistant.Service;

import com.example.aiassistant.Interfaces.PlannerServiceInterface;
import com.example.aiassistant.model.PlannerOutput;
import com.example.aiassistant.model.RequestState;
import org.springframework.stereotype.Service;

@Service
public class PlannerServiceInterfaceImpl implements PlannerServiceInterface {
    //this decides if ai can revert the answer or not and if not then research tool will be used,
    // this is decided by istoolneeded.
    // And also it generates the intent of the message like direct definition , query, reasearch report etc.
    // If istoolneeded is true then the task is divided into subtasks if needed
    // I will use groq or deepseek ai integration

    //Questions : will ai answer the first question or how will I decide if ai can revert or not
    //acc to that istoolneeded will be set
    //ai will only tell the intent also
    //will have to see how the task will be divided into subtasks, rn hat i am thinking is that ai itelf divides task A inot A.1 and A.2, A.3 etc
    //using these subtasks we can feed the data to research tool and loop over the subtasks list

    private final LlmServiceInterfaceImpl llmServiceInterfaceImpl;

    public PlannerServiceInterfaceImpl(LlmServiceInterfaceImpl llmServiceInterfaceImpl){
        this.llmServiceInterfaceImpl = llmServiceInterfaceImpl;
    }

    @Override
    public RequestState plan(RequestState state){
        String prompt =  buildPrompt(state.getMessageRequest());
        PlannerOutput output = llmServiceInterfaceImpl.callStruc(prompt, PlannerOutput.class);

        state.setIntent(output.intent());
        state.setExternalResearch(output.isExternalResearch());
        state.setSubTasks(output.subTasks());

        return state;
    }

    private String buildPrompt(String prompt){
        return """
            You are a planning assistant for a ai assistant application.
            Given a user's question, decide:

            1. intent: classify the question as one of: factual, comparison, recommendation, casual
            2. toolNeeded: true if answering requires current/live information
               (things that change over time — prices, news, versions, rankings, recent events),
               false if it's stable/timeless knowledge you already know well.
            3. subTasks: ONLY if toolNeeded is true, break the question into one or more
               specific, searchable sub-questions. Each sub-task needs a topic (short label)
               and a subTaskMessage (the actual sub-question search query text).
               If toolNeeded is false, return an empty list for subTasks.

            User question: %s
            """.formatted(prompt);
    }

}
