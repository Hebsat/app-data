package org.example.services;

import org.example.model.User;
import org.example.repositories.UserRepository;
import org.example.request.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(Auth auth) {
        return userRepository.findByUsername(auth.getName());
    }

    public boolean validateUser(Auth auth, User user) {
        return user.getPassword().equals(auth.getPassword());
    }
}
