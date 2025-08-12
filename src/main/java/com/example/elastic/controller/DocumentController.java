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
        if (document.getIdentifier() == null || document.getIdentifier().isBlank()) {
            return ResponseEntity.badRequest().body("identifier should not be empty");
        }
        if (document.getBody() == null) {
            return ResponseEntity.badRequest().body("body can't be null");
        }
        return ResponseEntity.ok("saved");
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
