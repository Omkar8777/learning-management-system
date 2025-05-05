package com.example.lms.repository;

import com.example.lms.model.Attendance;
import com.example.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByUser(User user);
}
