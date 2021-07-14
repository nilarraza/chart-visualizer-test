package com.eitech1.chartv.service.interfaces;

import com.eitech1.chartv.Entity.User;
import com.eitech1.chartv.request.ChangePasswordRequest;

import java.util.Optional;

public interface IAdminService {
    Optional<User> findUserByUserName(String email);

    void changePassword(User user);

    int updatePassword(ChangePasswordRequest req);
}
