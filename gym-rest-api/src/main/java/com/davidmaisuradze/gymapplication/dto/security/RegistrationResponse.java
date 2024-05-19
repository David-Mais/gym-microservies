package com.davidmaisuradze.gymapplication.dto.security;

import com.davidmaisuradze.gymapplication.dto.CredentialsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponse {
    private CredentialsDto credentials;
    private TokenDto token;
}
