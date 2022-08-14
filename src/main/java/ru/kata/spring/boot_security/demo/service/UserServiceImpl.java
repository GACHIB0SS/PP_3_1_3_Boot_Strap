package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    @Transactional
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    @Transactional
    public void createUser(User user) {
        user.setPassword(bCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.createUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        boolean passwordIsNotChanged = userRepository.readUser(user.getId()).getPassword().equals(user.getPassword());
        if (!passwordIsNotChanged) {
            user.setPassword(bCryptPasswordEncoder().encode(user.getPassword()));
        }
        userRepository.updateUser(user);
    }

    @Override
    @Transactional
    public User readUser(Long id) {
        return userRepository.readUser(id);
    }

    @Override
    @Transactional
    public List<User> readAllUsers() {
        return userRepository.readAllUsers();
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}