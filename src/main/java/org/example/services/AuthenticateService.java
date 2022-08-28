package org.example.services;

import org.example.model.User;
import org.example.request.Auth;

public interface AuthenticateService {

    User getUser(Auth auth);

    boolean validateUser(Auth auth, User user);
}
