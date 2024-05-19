package com.davidmaisuradze.gymapplication.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    private Long id;
    private String token;
    private String username;
    private Date expiredAt;
}
