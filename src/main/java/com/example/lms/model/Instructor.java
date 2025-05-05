package com.example.lms.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {

    @Id
    private String instructorId; 
    private     String name;
    private String department;
}
