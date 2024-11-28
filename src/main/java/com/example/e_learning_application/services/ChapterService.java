package com.example.e_learning_application.services;

import com.example.e_learning_application.entities.Chapter;
import com.example.e_learning_application.repositories.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    public Chapter addChapter(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    public List<Chapter> getChaptersByCourseId(String courseId) {
        return chapterRepository.findByCourseId(courseId);
    }

    public Optional<Chapter> getChapterById(String chapterId) {
        return chapterRepository.findByIdChapter(chapterId);
    }

    public void deleteChapter(String chapterId) {
        chapterRepository.deleteById(chapterId);
    }
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }
}
