package com.eitech1.chartv.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private Long id;
    private String oldPassword;
    private String newPassword;
}
