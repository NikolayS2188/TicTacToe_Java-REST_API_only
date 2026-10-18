package com.karkaror.tictactoe.datasource.model;

import com.karkaror.tictactoe.domain.model.Role;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

/**
 * Сущность пользователя для хранения в базе данных.
 * Роли хранятся как отдельная таблица (ElementCollection).
 */
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private UUID id;

    @Column(unique = true, nullable = false, length = 50)
    private String login;

    @Column(nullable = false, length = 100)
    private String password;

    public UserEntity() {}

    /**
     * Роли пользователя.
     * <p>{@code @ElementCollection} — создаёт отдельную таблицу user_roles для коллекции простых типов.
     * <p>{@code @Enumerated(STRING)} — храним роль как строку, а не как ord.
     * <p>{@code @CollectionTable} — имя таблицы и колонок.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    public UserEntity(UUID id, String login, String password, List<Role> roles) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.roles = roles;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Role> getRoles() { return roles; }
    public void setRoles(List<Role> roles) { this.roles = roles; }
}
