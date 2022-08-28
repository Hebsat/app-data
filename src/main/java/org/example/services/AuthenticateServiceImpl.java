package org.example.services;

import org.example.model.User;
import org.example.repositories.UserRepository;
import org.example.request.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AuthenticateServiceImpl implements AuthenticateService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser(Auth auth) {
        Logger.getLogger(AuthenticateServiceImpl.class.getName()).info("getting user: " + auth.getName());
        return userRepository.findByUsername(auth.getName());
    }

    @Override
    public boolean validateUser(Auth auth, User user) {
        Logger.getLogger(AuthenticateServiceImpl.class.getName()).info("validating password of user: " + auth.getName() + user.getUsername() + user.getPassword());
        return user.getPassword().equals(auth.getPassword());
    }
}
