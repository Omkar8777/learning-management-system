package com.example.lms.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.lms.model.Instructor;
import com.example.lms.repository.InstructorRepository;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    @Autowired
    private InstructorRepository instructorRepository;

    @PostMapping
    public Instructor addInstructor(@RequestBody Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    @GetMapping
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }
}
