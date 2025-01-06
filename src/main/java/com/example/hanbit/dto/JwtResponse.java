package com.example.hanbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String username;
    private String name;
    private String email;
    private String tel;
    @JsonProperty("foreignYN")
    private boolean isForeigner;
}
