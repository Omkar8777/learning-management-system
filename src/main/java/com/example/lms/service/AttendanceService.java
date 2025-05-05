// package com.example.lms.service;

// import com.example.lms.dto.AttendanceDTO;
// import com.example.lms.model.Attendance;
// import com.example.lms.model.User;
// import com.example.lms.repository.AttendanceRepository;
// import com.example.lms.repository.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class AttendanceService {

//     @Autowired
//     private AttendanceRepository attendanceRepository;

//     @Autowired
//     private UserRepository userRepository;

//     public Attendance recordAttendance(AttendanceDTO dto) {
//         User user = null;

//         if (dto.getStudentId() != null) {
//             user = userRepository.findByStudentId(dto.getStudentId())
//                     .orElseThrow(() -> new IllegalArgumentException("Student not found"));
//         } else if (dto.getInstructorId() != null) {
//             user = userRepository.findByInstructorId(dto.getInstructorId())
//                     .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
//         } else {
//             throw new IllegalArgumentException("Student or Instructor ID must be provided");
//         }

//         Attendance attendance = new Attendance();
//         attendance.setUser(user);
//         attendance.setDate(dto.getDate());
//         attendance.setStatus(dto.getStatus());

//         return attendanceRepository.save(attendance);
//     }

//     public List<Attendance> getAttendanceByStudentId(String studentId) {
//         User user = userRepository.findByStudentId(studentId)
//                 .orElseThrow(() -> new IllegalArgumentException("Student not found"));
//         return attendanceRepository.findByUser(user);
//     }

//     public List<Attendance> getAttendanceByInstructorId(String instructorId) {
//         User user = userRepository.findByInstructorId(instructorId)
//                 .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
//         return attendanceRepository.findByUser(user);
//     }

//     public List<Attendance> getAttendanceByName(String name) {
//         User user = userRepository.findByName(name)
//                 .orElseThrow(() -> new IllegalArgumentException("User not found"));
//         return attendanceRepository.findByUser(user);
//     }
// }
package com.example.lms.service;

import com.example.lms.dto.AttendanceDTO;
import com.example.lms.model.Attendance;
import com.example.lms.model.User;
import com.example.lms.repository.AttendanceRepository;
import com.example.lms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@Slf4j
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    public Attendance recordAttendance(AttendanceDTO dto) {
        // Check if the current user has permissions to mark attendance
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthorized = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_HOD")) ||
                               authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"));
        
        if (!isAuthorized) {
            log.error("Unauthorized access: User {} attempted to mark attendance", authentication.getName());
            throw new SecurityException("Only HOD and instructors can mark attendance");
        }
        
        
        User student = userRepository.findByStudentId(dto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + dto.getStudentId()));
                
        
        Attendance attendance = new Attendance();
        attendance.setUser(student);
        attendance.setDate(dto.getDate());
        attendance.setStatus(dto.getStatus());

        log.info("Attendance marked for student {} by {}", student.getName(), authentication.getName());
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceByStudentId(String studentId) {
        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return attendanceRepository.findByUser(user);
    }

    public List<Attendance> getAttendanceByInstructorId(String instructorId) {
        User user = userRepository.findByInstructorId(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
        return attendanceRepository.findByUser(user);
    }

    public List<Attendance> getAttendanceByName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return attendanceRepository.findByUser(user);
    }
}