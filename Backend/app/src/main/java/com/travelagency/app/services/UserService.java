package com.travelagency.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelagency.app.entities.User;
import com.travelagency.app.repositories.UserRepository;

@Service

public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User saveUser(User user) throws Exception{

        if(userRepository.existsByEmail(user.getEmail())){
            throw new Exception("Error: The email already have an account");
        }

        if(userRepository.existsById(user.getUserId())){
            throw new Exception("Error: The user is already signin");
        }

        return userRepository.save(user);
    }

    public User findById(String user_id){

        User user = userRepository.findByUserId(user_id);

        return user;
    }

    public User findByUsername(String username) throws Exception {
        
        User user = userRepository.findByUsername(username);
        
        if(user == null){
            throw new Exception("Error: The user doesn't exist");
        }

        return user;
    }

    public User updateUser(User userDetails) throws Exception {
        User user = userRepository.findByUsername(userDetails.getUsername());

        if(user == null){
            throw new Exception("Error: The user doesn't exist");
        }
        user.setFullName(userDetails.getFullName());
        user.setEmail(userDetails.getEmail());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setNationality(userDetails.getNationality());
        user.setDocumentNumber(userDetails.getDocumentNumber());

        return userRepository.save(user);
    }

    public User desactivateUser(User user){
        
        user.setActiveUser(false);

        return userRepository.save(user);
    }
    
}
