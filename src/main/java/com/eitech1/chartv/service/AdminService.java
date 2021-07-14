package com.eitech1.chartv.service;

import com.eitech1.chartv.Entity.MRoles;
import com.eitech1.chartv.Entity.Roles;
import com.eitech1.chartv.controller.MailController;
import com.eitech1.chartv.respository.RolesRepository;
import com.eitech1.chartv.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.eitech1.chartv.Entity.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class AdminService {
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    public UserService userservice;
    @Autowired
    public MailController mailController;
    @Autowired
    public UserRepository userRepo;
    @Autowired
    public RolesRepository roleRepo;

    public AdminService(UserService userservice, MailController mailController) {
        this.userservice = userservice;
        this.mailController = mailController;
    }

    public User createUser(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            return null;
        }
        Random rmd = new Random();
        int r = 100000 + rmd.nextInt(999999);
        String password = encoder.encode(String.valueOf(r));
        user.setPassword(password);
        Set<Roles> roles = new HashSet<Roles>();
        Roles ROLEUSER = roleRepo.findByName(MRoles.ROLE_USER).get();
        roles.add(ROLEUSER);
        user.setRoles(roles);
        User u = userservice.create(user);
//        String password = encoder.encode(u.getPassword());
        if (u != null) {
            passwordMail(u.getEmail(), String.valueOf(r));
        }
        return u;
    }

    public void passwordMail(String email, String password) {

        MultiValueMap<String, String> mMap = new LinkedMultiValueMap<>();
        mMap.add("emailTo", email);
        mMap.add("emailFrom", "teamaliens.b18it@gmail.com");
        mMap.add("emailSubject", "Account Credentials");
        mMap.add("emailContent", "Hello from Eagle Eye Tech One " + "\n" + "Please use the following password to log in to the system " + "\n " + password + "\n"
                + "Please be kind to change the password soon after your first login"
        );

        mailController.sendmail(mMap);

    }

}
