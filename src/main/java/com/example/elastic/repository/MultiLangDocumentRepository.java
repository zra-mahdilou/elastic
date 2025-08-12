package com.example.elastic.repository;

import com.example.elastic.entity.MultiLangDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MultiLangDocumentRepository extends ElasticsearchRepository<MultiLangDocument, String> {

    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"body.*\"]}}")
    List<MultiLangDocument> findByBodyText(String text);
}
