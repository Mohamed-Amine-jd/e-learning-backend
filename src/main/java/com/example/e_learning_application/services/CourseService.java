package com.example.e_learning_application.services;

import com.example.e_learning_application.entities.Course;
import com.example.e_learning_application.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> getCourseById(String courseId) {
        return courseRepository.findByCourseId(courseId);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public void deleteCourse(String courseId) {
        courseRepository.deleteById(courseId);
    }

    public Course updateCourse(String courseId, Course updatedCourse) {
        Optional<Course> existingCourseOpt = courseRepository.findByCourseId(courseId);
        if (existingCourseOpt.isPresent()) {
            Course existingCourse = existingCourseOpt.get();

            // Update course fields with the new data
            existingCourse.setName(updatedCourse.getName());
            existingCourse.setDescription(updatedCourse.getDescription());
            existingCourse.setImage(updatedCourse.getImage());
            existingCourse.setField(updatedCourse.getField());
            existingCourse.setTrainerId(updatedCourse.getTrainerId());
            existingCourse.setChapters(updatedCourse.getChapters());

            // Save the updated course
            return courseRepository.save(existingCourse);
        } else {
            // If the course does not exist, return null (or you can throw an exception)
            return null;
        }
    }

    public Course acceptCourse(String courseId) {
        Optional<Course> courseOpt = courseRepository.findByCourseId(courseId);

        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();

            // Update the status to "Accepted"
            course.setAccepted("Accepted");
            return courseRepository.save(course);
        } else {
            throw new RuntimeException("Course not found with ID: " + courseId);
        }
    }
}
