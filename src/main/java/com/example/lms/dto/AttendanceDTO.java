// package com.example.lms.dto;

// import lombok.Data;

// @Data
// public class AttendanceDTO {
//     private String date;
//     private String status;
//     private String studentId;
//     private String instructorId;
// }
package com.example.lms.dto;

import com.example.lms.model.AttendanceStatus;
import lombok.Data;

@Data
public class AttendanceDTO {
    private String date;
    private AttendanceStatus status; 
    private String studentId;
    private String instructorId;
}
