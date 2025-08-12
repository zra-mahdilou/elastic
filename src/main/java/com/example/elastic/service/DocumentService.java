package com.example.elastic.service;

import com.example.elastic.dto.DocumentDTO;
import com.example.elastic.entity.MultiLangDocument;

import java.util.List;
import java.util.Optional;

public interface DocumentService {

    public MultiLangDocument save(MultiLangDocument doc);
    public List<MultiLangDocument> search(String q, String lang, int size);
    public List<MultiLangDocument> searchBySimpleText();
}
