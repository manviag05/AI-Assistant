package com.example.aiassistant.Service;

import com.example.aiassistant.Interfaces.LlmServiceInterface;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.ChatClient;

@Service
public class LlmServiceInterfaceImpl implements LlmServiceInterface {

    private final ChatClient chatClient;

    public LlmServiceInterfaceImpl(ChatClient.Builder builder){
        this.chatClient = builder.build();
    }
    //this wll contain all LLM specific logic
    @Override
    public String call(String prompt){
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    @Override
    public <T> T callStruc(String prompt, Class<T> ResponseType){
        return chatClient.prompt()
                .user(prompt)
                .call()
                .entity(ResponseType);
    }
}
