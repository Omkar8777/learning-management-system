package com.example.lms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(unique = true, nullable = false)
    private String phone;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "course")
    private String course; 

    @Column(name = "student_id")
    private String studentId; 

    @Column(name = "instructor_id")
    private String instructorId; 

    @Column(name = "department")
    private String department; 
    // Add this to your User entity
    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "studentId", insertable = false, updatable = false)
    private Student student;



}