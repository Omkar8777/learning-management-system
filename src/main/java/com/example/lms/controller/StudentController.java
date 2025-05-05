package com.example.lms.controller;

import com.example.lms.model.Student;
import com.example.lms.model.User;
import com.example.lms.repository.StudentRepository;
import com.example.lms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@Slf4j
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private UserRepository userRepository;

  
    @GetMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('HOD', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<Student> getStudentById(@PathVariable String studentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("Fetching student with ID: {} - requested by: {}", studentId, username);
        
       
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_STUDENT"))) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            if (!user.getStudentId().equals(studentId)) {
                log.warn("Student {} attempted to access another student's record", username);
                throw new RuntimeException("Students can only view their own records");
            }
        }
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        return ResponseEntity.ok(student);
    }
}