package com.example.oj.repository;

import com.example.oj.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface UserRepository extends MongoRepository<UserDocument, String> {
    UserDocument insert(UserDocument userDocument);
    UserDocument findOneByEmail(String email);

    Optional<UserDocument> findByEmail(String email);
}
