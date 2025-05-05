package com.example.lms.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lms.model.Student;

public interface StudentRepository extends JpaRepository<Student, String> {}
