package com.eitech1.chartv.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ForgotUsernameOrPasswordRequest {
    @NotNull
    @NotEmpty
    private String email;
}
