//package com.example.elastic;
//
//import com.example.elastic.dto.DocumentDTO;
//import com.example.elastic.entity.MultiLangDocument;
//import com.example.elastic.repository.MultiLangDocumentRepository;
//import com.example.elastic.service.impl.DocumentServiceImpl;
//import com.example.elastic.mapper.DocumentMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//public class DocumentServiceTest {
//
//    @Mock
//    private MultiLangDocumentRepository repository;
//
//    @Mock
//    private ElasticsearchOperations elasticsearchOperations;
//
//    @Mock
//    private DocumentMapper mapper;
//
//    @InjectMocks
//    private DocumentServiceImpl service;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testSaveDocument() {
//        DocumentDTO dto = new DocumentDTO();
//        dto.setIdentifier("1001");
//        dto.setBody(Map.of("fa", "حالتون چطوره"));
//
//        MultiLangDocument entity = new MultiLangDocument();
//        entity.setIdentifier("1001");
//        entity.setBody(Map.of("fa", "حالتون چطوره"));
//
//        when(mapper.toEntity(dto)).thenReturn(entity);
//        when(repository.save(entity)).thenReturn(entity);
//
//        MultiLangDocument saved = service.save(dto);
//
//        verify(mapper).toEntity(dto);
//        verify(repository).save(entity);
//
//        assertNotNull(saved);
//        assertEquals("1001", saved.getIdentifier());
//    }
//
//    @Test
//    public void testSearchByText() {
//        String searchText = "حالتون";
//
//        DocumentDTO dto = new DocumentDTO();
//        dto.setIdentifier("1001");
//        dto.setBody(Map.of("fa", "حالتون چطوره"));
//
//        MultiLangDocument entity = new MultiLangDocument();
//        entity.setIdentifier("1001");
//        entity.setBody(Map.of("fa", "حالتون چطوره"));
//
//        SearchHit<MultiLangDocument> searchHit = mock(SearchHit.class);
//        when(searchHit.getContent()).thenReturn(entity);
//
//        SearchHits<MultiLangDocument> searchHits = mock(SearchHits.class);
//        when(searchHits.getSearchHits()).thenReturn(List.of(searchHit));
//
//        when(elasticsearchOperations.search(any(NativeSearchQuery.class), eq(MultiLangDocument.class))).thenReturn(searchHits);
//        when(mapper.toDTO(entity)).thenReturn(dto);
//
//        List<DocumentDTO> results = service.searchByText(searchText);
//
//        verify(elasticsearchOperations).search(any(NativeSearchQuery.class), eq(MultiLangDocument.class));
//        verify(mapper).toDTO(entity);
//
//        assertNotNull(results);
//        assertEquals(1, results.size());
//        assertEquals("1001", results.get(0).getIdentifier());
//    }
//}
