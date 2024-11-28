package com.example.e_learning_application.repositories;

import com.example.e_learning_application.entities.StudentInterest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InterestRepository extends MongoRepository<StudentInterest, String> {
    Optional<StudentInterest> findByEmail(String email);
}
