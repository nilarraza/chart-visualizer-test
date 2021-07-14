package com.eitech1.chartv.service;

import com.eitech1.chartv.controller.MailController;
import com.eitech1.chartv.Entity.User;
import org.springframework.stereotype.Service;
import com.eitech1.chartv.respository.UserRepository;

import javax.swing.text.html.Option;
import java.util.Optional;


@Service
public class UserService {
    public UserRepository userRepository;
    public MailController mailController;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User create(User user) {
        return userRepository.save(user);
    }

}
