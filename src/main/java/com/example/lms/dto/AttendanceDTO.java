package com.example.lms.dto;

import lombok.Data;

@Data
public class AttendanceDTO {
    private String date;
    private String status;
    private String studentId;
    private String instructorId;
}
