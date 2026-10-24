package br.com.supermarket_api.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.supermarket_api.dto.user.UserCollection;
import br.com.supermarket_api.dto.user.UserResponse;
import br.com.supermarket_api.model.User;
import br.com.supermarket_api.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // index
    public UserCollection findUsers(Map<String, Object> data) {
        int perPage = (int) data.getOrDefault("per_page", 15);

        Pageable pageable = PageRequest.of(0, perPage, Sort.by("createdAt").descending());
        Page<User> users = repository.findAll(pageable);

        return new UserCollection(users);
    }

    // show
    public UserResponse findById(Map<String, Object> data) {
        UUID id = (UUID) data.get("id");
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserResponse.from(user);
    }

    // store (ainda falta o hash da senha, veja abaixo)
    public UserResponse createUser(Map<String, Object> data) {
        User user = User.builder()
                .name((String) data.get("name"))
                .password(passwordEncoder.encode((String) data.get("password")))
                .build();
        return UserResponse.from(repository.save(user));
    }

    // update
    public UserResponse updateUser(Map<String, Object> data) {
        UUID id = (UUID) data.get("id");
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName((String) data.get("name"));
        return UserResponse.from(repository.save(user));
    }

    // delete
    public void deleteUser(Map<String, Object> data) {
        UUID id = (UUID) data.get("id");
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        repository.delete(user);
    }
}