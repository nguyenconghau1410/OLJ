package com.example.oj.service;

import com.example.oj.document.RoleDocument;
import com.example.oj.document.UserDocument;
import com.example.oj.repository.RoleRepository;
import com.example.oj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserDocument insert(UserDocument userDocument) {
        return userRepository.insert(userDocument);
    }
    public UserDocument findOneByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    public Optional<UserDocument> findUser(String email) {
        return userRepository.findByEmail(email);
    }
    public void updateUser(String email, Map<String, String> data) {
        UserDocument userDocument = userRepository.findOneByEmail(email);
        userDocument.setName(data.get("name"));
        userDocument.setFaculty(data.get("faculty"));
        userDocument.setClassname(data.get("classname"));
        userRepository.save(userDocument);
    }
}
