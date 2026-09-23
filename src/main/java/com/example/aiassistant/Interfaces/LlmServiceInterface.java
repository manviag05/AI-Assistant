package com.example.aiassistant.Interfaces;

public interface LlmServiceInterface {
    String call(String prompt);
    <T> T callStruc(String prompt, Class<T> ResponseType);
}
