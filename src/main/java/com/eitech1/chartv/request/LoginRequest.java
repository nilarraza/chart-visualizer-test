package com.eitech1.chartv.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;


}
