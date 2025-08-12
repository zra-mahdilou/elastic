package com.example.elastic.dto;

import java.util.Map;

public class DocumentDTO {
    private String identifier;
    private Map<String, String> body;

    public String getIdentifier() {
        return identifier;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }
}
