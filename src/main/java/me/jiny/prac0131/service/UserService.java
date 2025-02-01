package me.jiny.prac0131.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.dto.UserRequest;
import me.jiny.prac0131.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import me.jiny.prac0131.domain.User;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long save(UserRequest userRequest) {
        return userRepository.save(User.builder()
                .username(userRequest.getUsername())
                .password(bCryptPasswordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .build()).getId();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Unexpected User"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean checkUsernameDuplicate(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean checkEmailDuplicate(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isIdPasswordValid(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null && bCryptPasswordEncoder.matches(password, user.getPassword());
    }

}