package com.example.lms.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lms.model.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, String> {}
