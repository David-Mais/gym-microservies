package com.davidmaisuradze.gymapplication.service;

import com.davidmaisuradze.gymapplication.dto.CredentialsDto;
import com.davidmaisuradze.gymapplication.dto.PasswordChangeDto;
import com.davidmaisuradze.gymapplication.dto.security.TokenDto;

public interface AuthenticationService {
    TokenDto login(CredentialsDto credentialsDto);
    TokenDto changePassword(String username, PasswordChangeDto passwordChangeDto);
}
