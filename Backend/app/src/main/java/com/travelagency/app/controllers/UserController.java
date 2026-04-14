package com.travelagency.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelagency.app.entities.User;
import com.travelagency.app.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/user")
@CrossOrigin("*")

public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestParam String keycloak) throws Exception{

        User user = userService.findByKeycloak(keycloak);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) throws Exception{

        return ResponseEntity.ok(userService.saveUser(user));
    }
    
}
