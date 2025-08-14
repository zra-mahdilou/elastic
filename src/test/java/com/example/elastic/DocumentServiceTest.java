package com.example.elastic;

import com.example.elastic.dto.DocumentDTO;
import com.example.elastic.entity.MultiLangDocument;
import com.example.elastic.repository.MultiLangDocumentRepository;
import com.example.elastic.service.impl.DocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DocumentServiceTest {

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

    // ---------- Save Tests ----------

    @Test
    void testSaveDocument_WithValidData_ShouldSaveSuccessfully() {
        // Arrange
        DocumentDTO dto = new DocumentDTO("id1", Map.of("en", "hello"));
        MultiLangDocument entity = new MultiLangDocument("id1", Map.of("en", "hello"));

        when(repository.save(any(MultiLangDocument.class))).thenReturn(entity);

        // Act
        MultiLangDocument saved = documentService.save(dto);

        // Assert
        assertNotNull(saved);
        assertEquals("id1", saved.getIdentifier());
        assertEquals("hello", saved.getBody().get("en"));
        verify(repository, times(1)).save(any(MultiLangDocument.class));
    }

    @Test
    void testSaveDocument_WithNullIdentifier_ShouldThrowException() {
        DocumentDTO dto = new DocumentDTO(null, Map.of("en", "hello"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> documentService.save(dto)
        );

        assertEquals("identifier should not be empty", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void testSaveDocument_WithBlankIdentifier_ShouldThrowException() {
        DocumentDTO dto = new DocumentDTO("   ", Map.of("en", "hello"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> documentService.save(dto)
        );

        assertEquals("identifier should not be empty", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void testSaveDocument_WithNullBody_ShouldThrowException() {
        DocumentDTO dto = new DocumentDTO("id1", null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> documentService.save(dto)
        );

        assertEquals("body can't be null", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void testSaveDocument_WithEmptyBody_ShouldThrowException() {
        DocumentDTO dto = new DocumentDTO("id1", Map.of());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> documentService.save(dto)
        );

        assertEquals("body can't be null or empty", exception.getMessage());
        verify(repository, never()).save(any());
    }

    // ---------- Search Tests ----------

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
