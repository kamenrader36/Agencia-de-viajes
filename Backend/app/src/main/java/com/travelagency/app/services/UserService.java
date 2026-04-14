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

        if(userRepository.existsByKeycloak(user.getKeycloak())){
            throw new Exception("Error: The user is already signin");
        }

        return userRepository.save(user);
    }

    public User findByKeycloak(String keycloak) throws Exception{

        User userToLook = userRepository.findByKeycloak(keycloak);
        
        if(userToLook == null){
            throw new Exception("Error: The user doesnt exists");
        }

        return userToLook;
    }

    public User updateUser(User newUser, String userKeycloak) throws Exception{

        User oldUser = findByKeycloak(userKeycloak);
        
        oldUser.setFullName(newUser.getFullName());
        oldUser.setPhoneNumber(newUser.getPhoneNumber());
        oldUser.setNationality(newUser.getNationality());

        return userRepository.save(oldUser);
    }

    public User desactivateUser(User user){
        
        user.setActiveUser(false);

        return userRepository.save(user);
    }
    
}
