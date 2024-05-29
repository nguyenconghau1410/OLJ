package com.example.oj.api;

import com.example.oj.constant.Utils;
import com.example.oj.document.UserDocument;
import com.example.oj.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserAPI {
    private final UserService userService;
    private final Utils utils;
    @GetMapping("/get")
    public ResponseEntity<Object> getUser(@RequestHeader("Authorization") String authorizationHeader) {
        String email = utils.getEmailFromToken(authorizationHeader);
        if(email != null) {
            UserDocument userDocument = userService.findOneByEmail(email);
            return ResponseEntity.ok(userDocument);
        }
        return null;
    }

    @PutMapping("/update")
    public void updateUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> data) {
        String email = utils.getEmailFromToken(authorizationHeader);
        if(email != null) {
            userService.updateUser(email, data);
        }
    }

    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<UserDocument> getUserEmail(@PathVariable String email) {
        Optional<UserDocument> userDocument = userService.findUser(email);
        if(userDocument.isPresent()) {
            return ResponseEntity.ok(userDocument.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<UserDocument> getUserById(@PathVariable String id) {
        Optional<UserDocument> userDocumentOptional = userService.findUserById(id);
        if(userDocumentOptional.isPresent()) {
            return ResponseEntity.ok(userDocumentOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
