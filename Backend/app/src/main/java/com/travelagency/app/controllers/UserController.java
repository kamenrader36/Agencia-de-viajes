package com.travelagency.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.travelagency.app.entities.User;
import com.travelagency.app.services.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal Jwt jwt) throws Exception {

        String id_keycloak = jwt.getSubject();

        User user = userService.findById(id_keycloak);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User user, @AuthenticationPrincipal Jwt jwt) throws Exception {
  
        user.setUserId(jwt.getSubject());
        String preferredUsername = jwt.getClaimAsString("preferred_username");
        user.setUsername(preferredUsername != null ? preferredUsername : jwt.getSubject());
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            user.setRole(roles.isEmpty() ? "USER" : roles.get(0).toUpperCase());
        } else {

            user.setRole("USER");
        }

        user.setActiveUser(true);

        return ResponseEntity.ok(userService.saveUser(user));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserProfile(@PathVariable String username) throws Exception {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateProfile(@RequestBody User user) throws Exception {
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }
}
