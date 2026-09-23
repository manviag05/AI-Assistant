package com.example.aiassistant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Finding {
    private String topic;
    private String researchResponse;
    private String sourceUrl;
}
