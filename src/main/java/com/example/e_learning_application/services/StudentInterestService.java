package com.example.e_learning_application.services;


import com.example.e_learning_application.entities.StudentInterest;
import com.example.e_learning_application.repositories.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentInterestService {

    @Autowired
    private InterestRepository IntersetRepository;


    public StudentInterest saveInterest(String email, List<String> interests) {
        StudentInterest studentInterest = new StudentInterest(email, interests);
        return IntersetRepository.save(studentInterest);
    }
}