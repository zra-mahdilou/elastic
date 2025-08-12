package com.example.elastic.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ElasticsearchIndexConfig {

    private final ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public ElasticsearchIndexConfig(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @PostConstruct
    public void createIndexAndMappingIfNotExist() {
        IndexOperations indexOps = elasticsearchOperations.indexOps(IndexCoordinates.of("multilang-documents"));

        if (!indexOps.exists()) {
            indexOps.create();

            Map<String, Object> properties = new HashMap<>();

            Map<String, Object> identifier = new HashMap<>();
            identifier.put("type", "keyword");
            properties.put("identifier", identifier);

            Map<String, Object> body = new HashMap<>();
            body.put("type", "object");
            body.put("dynamic", true);
            properties.put("body", body);

            Map<String, Object> mapping = new HashMap<>();
            mapping.put("properties", properties);

            // Dynamic template for body.* to be text
            mapping.put("dynamic_templates", new Object[]{
                    Map.of("strings_in_body", Map.of(
                            "path_match", "body.*",
                            "match_mapping_type", "string",
                            "mapping", Map.of("type", "text")
                    ))
            });

            indexOps.putMapping(Document.from(mapping));
        }
    }
}
