package com.eitech1.chartv.response.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserDetailsResponse {
    private String token;
    private List<String> roles;

    public UserDetailsResponse(String accessToken, List<String> roles) {
        this.token = accessToken;
        this.roles = roles;
    }

}
