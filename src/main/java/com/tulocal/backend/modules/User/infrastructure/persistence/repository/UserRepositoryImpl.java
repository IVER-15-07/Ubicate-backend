package com.tulocal.backend.modules.User.infrastructure.persistence.repository;

import com.tulocal.backend.modules.User.domain.model.User;
import com.tulocal.backend.modules.User.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(java.util.UUID.fromString(rs.getString("id")));
        user.setNombre(rs.getString("nombre"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        Object roleId = rs.getObject("role_id");
        user.setRoleId(roleId == null ? null : ((Number) roleId).intValue());
        java.sql.Timestamp createdAt = rs.getTimestamp("creado_en");
        if (createdAt != null) {
            user.setCreadoEn(createdAt.toLocalDateTime());
        }
        return user;
    };

    @Override
    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE LOWER(email) = LOWER(?)",
                Integer.class,
                email
        );
        return Objects.requireNonNullElse(count, 0) > 0;
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (nombre, email, password_hash, role_id) VALUES (?, ?, ?, ?) " +
                "RETURNING id, nombre, email, password_hash, role_id, creado_en";
        return jdbcTemplate.queryForObject(
                sql,
                userRowMapper,
                user.getNombre(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRoleId()
        );
    }
}