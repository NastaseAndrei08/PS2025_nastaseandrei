package com.example.demo.controller;

import com.example.demo.dto.userdto.UserDTO;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;

import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final RestTemplate restTemplate;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        user.setTimeStamp(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findRoleByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role USER not found"));

        user.setRole(role);
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        return userRepository.findUserByEmail(loginRequest.getEmail())
                .filter(user -> passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                .map(user -> {
                    //  Verificare dacă userul e blocat
                    String url = "http://localhost:8083/moderate/is-blocked?email=" + user.getEmail();
                    boolean isBlocked = false;
                    try {
                        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
                        isBlocked = Boolean.TRUE.equals(response.getBody());
                    } catch (Exception ignored) {}

                    if (isBlocked) {
                        return ResponseEntity.status(403).body("Your account has been blocked.");
                    }

                    // Generare token dacă nu e blocat
                    return ResponseEntity.ok(jwtService.generateToken(user.getEmail()));
                })
                .orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }


    @GetMapping("/me")
    public ResponseEntity<String> whoAmI(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtService.extractUsername(token);
            return ResponseEntity.ok("You are: " + email);
        }
        return ResponseEntity.status(401).body("Missing token");
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestBody String token) {
        String email = jwtService.extractUsername(token);
        return ResponseEntity.ok(email);
    }

    @PostMapping("/validate/user")
    public ResponseEntity<UserDTO> validateTokenAndGetUser(@RequestBody String token) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRoleName(user.getRole().getName());

        return ResponseEntity.ok(dto);
    }





}
