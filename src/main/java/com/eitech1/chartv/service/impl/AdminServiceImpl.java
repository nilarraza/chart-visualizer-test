package com.eitech1.chartv.service.impl;

import com.eitech1.chartv.Entity.User;
import com.eitech1.chartv.request.ChangePasswordRequest;
import com.eitech1.chartv.respository.UserRepository;
import com.eitech1.chartv.service.interfaces.IAdminService;
import com.eitech1.chartv.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements IAdminService, UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmailUtil email;

    @Override
    public Optional<User> findUserByUserName(String email) {
        return userRepo.findByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optional = findUserByUserName(email);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        User user = optional.get();
        return UserDetailsImpl.build(user);
    }


    @Override
    public int updatePassword(ChangePasswordRequest req) {
        User user = userRepo.findById(req.getId()).get();
        if (encoder.matches(req.getOldPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(req.getNewPassword()));
            userRepo.save(user);
            String message = "Hello from Eagle Eye Tech One \n"
                    + "Successfully Updated the Password \n"
                    + "This message was was sent for the request of update password";
            email.sendEmail(user.getEmail(), "Change Password", message);
            return 1;
        }
        return 0;
    }

    @Override
    public void changePassword(User user) {
        String password = encoder.encode(user.getPassword()).substring(0, 10);
        user.setPassword(encoder.encode(password));
        userRepo.save(user);
        String message = "Hello from Eagle Eye Tech One \n"
                + "Please use the following password to log in to the system " + "\n " + password + "\n"
                + "This message was was sent for the request of password change";
        email.sendEmail(user.getEmail(), "Request for Password Change", message);

    }
}
