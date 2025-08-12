package com.example.elastic.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;

@Document(indexName = "multilang-documents")
public class MultiLangDocument {

    @Id
    private String identifier;

    @Field(type = FieldType.Object)
    private Map<String , String> body ;

    public MultiLangDocument(String identifier, Map<String, String> body) {
        this.identifier = identifier;
        this.body = body;
    }

    public MultiLangDocument() {

    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Map<String, String> getBody() {
        return body;
    }
}
