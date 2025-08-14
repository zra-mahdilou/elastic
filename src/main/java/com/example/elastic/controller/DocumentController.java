package com.example.elastic.controller;


import com.example.elastic.dto.DocumentDTO;
import com.example.elastic.entity.MultiLangDocument;
import com.example.elastic.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<?> saveDocument(@RequestBody DocumentDTO document) {
        try {
            MultiLangDocument saved = documentService.save(document);
            return ResponseEntity.created(URI.create("/documents/" + saved.getIdentifier())).body(saved);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @GetMapping("/search")
    public ResponseEntity<List<MultiLangDocument>> search(
            @RequestParam("q") String q,
            @RequestParam(value = "lang", required = false) String lang,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size
    ) {
        List<MultiLangDocument> results = documentService.search(q, lang, size);
        return ResponseEntity.ok(results);
    }
}
