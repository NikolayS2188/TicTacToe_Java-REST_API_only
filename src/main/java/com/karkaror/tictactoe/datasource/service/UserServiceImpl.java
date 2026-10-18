package com.karkaror.tictactoe.datasource.service;

import com.karkaror.tictactoe.datasource.mapper.UserDataMapper;
import com.karkaror.tictactoe.datasource.model.UserEntity;
import com.karkaror.tictactoe.datasource.repository.UserRepository;
import com.karkaror.tictactoe.domain.model.Role;
import com.karkaror.tictactoe.domain.model.User;
import com.karkaror.tictactoe.domain.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализация сервиса пользователей.
 * Обеспечивает регистрацию, поиск пользователей в БД, хэширование паролей.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserDataMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, UserDataMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String login, String password) {
        if (existsByLogin(login)) {
            throw new RuntimeException("User already exists");
        }

        String hashedPassword = passwordEncoder.encode(password); // хеширование
        User user = new User(UUID.randomUUID(), login, hashedPassword,
                List.of(Role.USER));
        UserEntity entity = mapper.toEntity(user);
        UserEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }
}
