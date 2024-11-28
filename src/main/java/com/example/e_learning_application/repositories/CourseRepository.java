package com.example.e_learning_application.repositories;

import com.example.e_learning_application.entities.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String> {
    Optional<Course> findByCourseId(String courseId);
    Optional<Course> findByTrainerId(String trainerId);
}
