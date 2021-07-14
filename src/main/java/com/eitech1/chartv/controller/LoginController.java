package com.eitech1.chartv.controller;

import com.eitech1.chartv.Entity.User;
import com.eitech1.chartv.request.ChangePasswordRequest;
import com.eitech1.chartv.request.ForgotUsernameOrPasswordRequest;
import com.eitech1.chartv.request.LoginRequest;
import com.eitech1.chartv.response.dto.UserDetailsResponse;
import com.eitech1.chartv.service.AdminService;
import com.eitech1.chartv.service.impl.AdminServiceImpl;
import com.eitech1.chartv.service.impl.UserDetailsImpl;
import com.eitech1.chartv.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/all")
public class LoginController {
    @Autowired
    private AdminServiceImpl service;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil util;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<UserDetailsResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        String token = util.generateToken(request.getEmail());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity
                .ok(new UserDetailsResponse(token, roles));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> forgotUsernameOrPassword(@Valid @RequestBody ForgotUsernameOrPasswordRequest fupr) {
        String email = fupr.getEmail();
        Optional<User> optional = service.findUserByUserName(email);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        User user = optional.get();
        service.changePassword(user);
        return ResponseEntity.ok("Password for the username " + email + " was successfully changed and Check your mail to update the password");
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody ChangePasswordRequest req) {
        int updatePassword = service.updatePassword(req);
        System.out.println(updatePassword);
        if (updatePassword != 0) {
            return ResponseEntity.ok("Password updated sucessfully");
        } else {
            return ResponseEntity.badRequest().body("Sorry Try Again: Old Password may be incorrect");
        }
    }
}
