package com.example.hanbit.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.hanbit.domain.RoleType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private Long id;
    private String username;
    private String name;
    private String email;
    private String tel;
    @JsonProperty("foreignYN")
    private boolean isForeigner;
    @Enumerated(EnumType.STRING)
    private RoleType role;
}
