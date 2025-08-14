# Multi-Language Document Search with Spring Boot & Elasticsearch

## Overview
This project is a Spring Boot application integrated with Elasticsearch to store and search multilingual documents.  
It allows saving documents with translations in different languages and performing full-text search on a specific language or across all available languages.

---

## Features
- **Save Documents** with multiple language fields.
- **Search by keyword** in a specific language or across all languages.
- **REST API** endpoints for managing and querying documents.
- **Optimized Elasticsearch Index Settings** to prevent excessive memory usage.

---

## The Problem I Faced
While running Elasticsearch locally, I encountered **high memory usage** due to **shard replication**.  
By default, Elasticsearch creates:
- **Primary shards**
- **Replica shards** (copies)

This replication increases reliability but consumes more RAM.  
Since this project is for local development, I set the **`number_of_replicas` to 0** so that only primary shards are created.

---

## Tech Stack
- **Java 21**

---

## API Endpoints

### 1. Save Document
**POST** `/documents`  
**Request Body:**
```json
{
  "identifier": "doc1",
  "translations": {
    "en": "Hello World",
    "fa": "سلام دنیا"
  }
}
  
