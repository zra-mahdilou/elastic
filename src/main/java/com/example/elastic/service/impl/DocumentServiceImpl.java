package com.example.elastic.service.impl;

import com.example.elastic.dto.DocumentDTO;
import com.example.elastic.entity.MultiLangDocument;
import com.example.elastic.repository.MultiLangDocumentRepository;
import com.example.elastic.service.DocumentService;
import com.example.elastic.mapper.DocumentMapper;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final MultiLangDocumentRepository repository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final DocumentMapper mapStruct ;


    public DocumentServiceImpl(MultiLangDocumentRepository repository, ElasticsearchOperations elasticsearchOperations, DocumentMapper mapStruct) {
        this.repository = repository;

        this.elasticsearchOperations = elasticsearchOperations;
        this.mapStruct = mapStruct;
    }

    public MultiLangDocument save(DocumentDTO dto)  {
        validateDocument(dto);
        MultiLangDocument entity = new MultiLangDocument();
        entity.setIdentifier(dto.getIdentifier());
        entity.setBody(dto.getBody());
        return repository.save(entity);
    }
    private void validateDocument(DocumentDTO dto) {
        if (dto.getIdentifier() == null || dto.getIdentifier().isBlank()) {
            throw new IllegalArgumentException("Identifier should not be empty");
        }
        if (dto.getBody() == null) {
            throw new IllegalArgumentException("Body can't be null or blank");
        }
    }

    public List<MultiLangDocument> search(String q, String lang, int size) {
        QueryBuilder qb;
        if (lang != null && !lang.isBlank()) {
            qb = QueryBuilders.matchQuery("body." + lang, q);
        } else {
            qb = QueryBuilders.queryStringQuery(q).field("body.*");
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(qb)
                .withMaxResults(size)
                .build();

        SearchHits<MultiLangDocument> hits = elasticsearchOperations.search(searchQuery, MultiLangDocument.class);
        return hits.stream().map(h -> h.getContent()).collect(Collectors.toList());
    }
    @Override
    public List<MultiLangDocument> searchBySimpleText() {
        Criteria criteria =new Criteria("Body").contains("test");
        CriteriaQuery query = new CriteriaQuery(criteria);
        return elasticsearchOperations.search(query,MultiLangDocument.class)
                .stream().map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
