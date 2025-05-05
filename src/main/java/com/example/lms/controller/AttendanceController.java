// package com.example.lms.controller;

// import com.example.lms.dto.AttendanceDTO;
// import com.example.lms.model.Attendance;
// import com.example.lms.service.AttendanceService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/attendance")
// public class AttendanceController {

//     @Autowired
//     private AttendanceService attendanceService;

//     @PostMapping("/mark")
//     public ResponseEntity<Attendance> markAttendance(@RequestBody AttendanceDTO dto) {
//         return ResponseEntity.ok(attendanceService.recordAttendance(dto));
//     }

//     @GetMapping("/student/{studentId}")
//     public ResponseEntity<List<Attendance>> getStudentAttendance(@PathVariable String studentId) {
//         return ResponseEntity.ok(attendanceService.getAttendanceByStudentId(studentId));
//     }

//     @GetMapping("/instructor/{instructorId}")
//     public ResponseEntity<List<Attendance>> getInstructorAttendance(@PathVariable String instructorId) {
//         return ResponseEntity.ok(attendanceService.getAttendanceByInstructorId(instructorId));
//     }

//     @GetMapping("/by-name/{name}")
//     public ResponseEntity<List<Attendance>> getAttendanceByName(@PathVariable String name) {
//         return ResponseEntity.ok(attendanceService.getAttendanceByName(name));
//     }
// }
package com.example.lms.controller;

import com.example.lms.dto.AttendanceDTO;
import com.example.lms.model.Attendance;
import com.example.lms.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@Slf4j
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/mark")
    @PreAuthorize("hasRole('HOD') or hasRole('INSTRUCTOR')")
    public ResponseEntity<Attendance> markAttendance(@RequestBody AttendanceDTO dto, @RequestHeader("Authorization") String authHeader) {
        log.info("Attendance marking request received for student: {}", dto.getStudentId());
        return ResponseEntity.ok(attendanceService.recordAttendance(dto));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('HOD', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<List<Attendance>> getStudentAttendance(@PathVariable String studentId) {
        log.info("Retrieving attendance for student: {}", studentId);
        return ResponseEntity.ok(attendanceService.getAttendanceByStudentId(studentId));
    }

    @GetMapping("/instructor/{instructorId}")
    @PreAuthorize("hasAnyRole('HOD', 'INSTRUCTOR')")
    public ResponseEntity<List<Attendance>> getInstructorAttendance(@PathVariable String instructorId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByInstructorId(instructorId));
    }

    @GetMapping("/by-name/{name}")
    @PreAuthorize("hasAnyRole('HOD', 'INSTRUCTOR')")
    public ResponseEntity<List<Attendance>> getAttendanceByName(@PathVariable String name) {
        return ResponseEntity.ok(attendanceService.getAttendanceByName(name));
    }
}