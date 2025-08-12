package com.example.elastic.mapper;
import com.example.elastic.dto.DocumentDTO;
import com.example.elastic.entity.MultiLangDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    MultiLangDocument toEntity (DocumentDTO dto);
    DocumentDTO toDTO (MultiLangDocument entity);
}
