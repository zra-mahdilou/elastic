package com.example.elastic;

import com.example.elastic.dto.DocumentDTO;
import com.example.elastic.entity.MultiLangDocument;
import com.example.elastic.repository.MultiLangDocumentRepository;
import com.example.elastic.service.impl.DocumentServiceImpl;
import com.example.elastic.mapper.DocumentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DocumentServiceTest {

    @Mock
    private MultiLangDocumentRepository repository;

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveDocument() {
        MultiLangDocument doc = new MultiLangDocument("id1", Map.of("en", "hello"));

        when(repository.save(doc)).thenReturn(doc);

        MultiLangDocument saved = documentService.save(doc);

        assertNotNull(saved);
        assertEquals("id1", saved.getIdentifier());

        verify(repository, times(1)).save(doc);
    }

    @Test
    void testSearchWithLang() {
        MultiLangDocument doc = new MultiLangDocument("id1", Map.of("en", "hello world"));
        SearchHit<MultiLangDocument> hit = mock(SearchHit.class);
        when(hit.getContent()).thenReturn(doc);
        SearchHits<MultiLangDocument> searchHits = mock(SearchHits.class);
        when(searchHits.stream()).thenReturn(Stream.of(hit));

        when(elasticsearchOperations.search(
                any(org.springframework.data.elasticsearch.core.query.Query.class),
                eq(MultiLangDocument.class)))
                .thenReturn(searchHits);

        List<MultiLangDocument> results = documentService.search("hello", "en", 10);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("id1", results.get(0).getIdentifier());

        verify(elasticsearchOperations, times(1))
                .search(any(org.springframework.data.elasticsearch.core.query.Query.class), eq(MultiLangDocument.class));
    }


    @Test
    void testSearchWithoutLang() {
        MultiLangDocument doc = new MultiLangDocument("id2", Map.of("en", "hello world", "fa", "سلام دنیا"));
        SearchHit<MultiLangDocument> hit = mock(SearchHit.class);
        when(hit.getContent()).thenReturn(doc);
        SearchHits<MultiLangDocument> searchHits = mock(SearchHits.class);
        when(searchHits.stream()).thenReturn(Stream.of(hit));

        when(elasticsearchOperations.search(
                any(org.springframework.data.elasticsearch.core.query.Query.class),
                eq(MultiLangDocument.class)))
                .thenReturn(searchHits);

        List<MultiLangDocument> results = documentService.search("hello", null, 5);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("id2", results.get(0).getIdentifier());

        verify(elasticsearchOperations, times(1))
                .search(any(org.springframework.data.elasticsearch.core.query.Query.class), eq(MultiLangDocument.class));
    }
}