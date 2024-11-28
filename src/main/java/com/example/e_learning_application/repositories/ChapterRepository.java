package com.example.e_learning_application.repositories;

import com.example.e_learning_application.entities.Chapter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends MongoRepository<Chapter, String> {
    List<Chapter> findByCourseId(String courseId);
    Optional<Chapter> findByIdChapter(String idChapter);
}
