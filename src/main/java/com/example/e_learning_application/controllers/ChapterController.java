package com.example.e_learning_application.controllers;

import com.example.e_learning_application.entities.Chapter;
import com.example.e_learning_application.services.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @GetMapping("/all")
    public List<Chapter> getAllChapters() {
        return chapterService.getAllChapters();
    }

    @PostMapping("/add")
    public ResponseEntity<Chapter> addChapter(@RequestBody Chapter chapter) {
        Chapter savedChapter = chapterService.addChapter(chapter);
        return new ResponseEntity<>(savedChapter, HttpStatus.CREATED);
    }

    @GetMapping("/course/{courseId}")
    public List<Chapter> getChaptersByCourseId(@PathVariable String courseId) {
        return chapterService.getChaptersByCourseId(courseId);
    }

    @GetMapping("/{chapterId}")
    public ResponseEntity<Chapter> getChapterById(@PathVariable String chapterId) {
        Optional<Chapter> chapter = chapterService.getChapterById(chapterId);
        return chapter.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{chapterId}")
    public ResponseEntity<Void> deleteChapter(@PathVariable String chapterId) {
        chapterService.deleteChapter(chapterId);
        return ResponseEntity.ok().build();
    }
}
