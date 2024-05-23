package com.example.oj.repository;

import com.example.oj.document.TopicDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TopicRepository extends MongoRepository<TopicDocument, String> {
    TopicDocument insert(TopicDocument topicDocument);
    List<TopicDocument> findAll();
}
