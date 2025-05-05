package com.example.lms.controller;

import com.example.lms.dto.*;
import com.example.lms.model.Instructor;
import com.example.lms.model.Role;
import com.example.lms.model.Student;
import com.example.lms.model.User;
import com.example.lms.repository.InstructorRepository;
import com.example.lms.repository.StudentRepository;
import com.example.lms.repository.UserRepository;
import com.example.lms.security.JwtUtil;
import com.example.lms.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepo;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        log.info("Registration request received for email: {}", request.getEmail());

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Passwords do not match!"));
        }

        if (userRepo.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already taken!"));
        }

        if (userRepo.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepo.existsByPhone(request.getPhone())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Phone number is already registered!"));
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setStudentId(request.getStudentId());
        user.setInstructorId(request.getInstructorId());
        user.setCourse(request.getCourse());
        user.setDepartment(request.getDepartment());

        userRepo.save(user);

        if (request.getRole() == Role.STUDENT) {
            Student student = new Student();
            student.setStudentId(request.getStudentId());
            student.setName(request.getName());
            student.setDepartment(request.getDepartment());
            student.setCourse(request.getCourse());
            studentRepository.save(student);
        } else if (request.getRole() == Role.INSTRUCTOR) {
            Instructor instructor = new Instructor();
            instructor.setInstructorId(request.getInstructorId());
            instructor.setName(request.getName());
            instructor.setDepartment(request.getDepartment());
            instructorRepository.save(instructor);
        }

        log.info("User registered successfully with role {}: {}", request.getRole(), request.getEmail());
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        log.info("Login request received for username: {}", request.getUsername());

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String jwt = jwtUtil.generateToken(userDetails);

            log.info("User logged in successfully: {}", request.getUsername());

            return ResponseEntity.ok(new AuthResponse(
                    jwt,
                    userDetails.getUsername(),
                    "User logged in successfully",
                    jwtUtil.getExpirationFromToken(jwt)
            ));

        } catch (BadCredentialsException e) {
            log.warn("Login failed for username {}: Bad credentials", request.getUsername());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: Invalid username or password"));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        try {
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                return ResponseEntity.ok(new AuthResponse(
                        token,
                        username,
                        "Token is valid",
                        jwtUtil.getExpirationFromToken(token))
                );
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Token validation failed"));
        }
    }
}
