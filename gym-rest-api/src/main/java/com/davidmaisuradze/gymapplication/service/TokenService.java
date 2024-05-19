package com.davidmaisuradze.gymapplication.service;

import com.davidmaisuradze.gymapplication.dto.security.RegistrationResponse;
import com.davidmaisuradze.gymapplication.security.GymUserDetails;

public interface TokenService {
    RegistrationResponse register(GymUserDetails user, String username, String password);
}

