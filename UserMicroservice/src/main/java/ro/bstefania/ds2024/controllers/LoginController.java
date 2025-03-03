package ro.bstefania.ds2024.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.bstefania.ds2024.dtos.PersonLoginDTO;
import ro.bstefania.ds2024.services.impl.AuthService;

import java.util.Collections;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/auth")
public class LoginController {
    private final AuthService authService;

    @Autowired
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody PersonLoginDTO loginDTO) {
        try {
            Map<String, Object> response = authService.authenticate(loginDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid credentials"));
        }
    }

    @PostMapping(value = "/check-account")
    public ResponseEntity<?> checkAccount(@RequestBody Map<String, String> usernameRequest) {
        boolean userExists = authService.doesUserExist(usernameRequest.get("username"));
        return ResponseEntity.ok(Collections.singletonMap("userExists", userExists));
    }
}
