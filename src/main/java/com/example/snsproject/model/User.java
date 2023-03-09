package com.example.snsproject.model;

import com.example.snsproject.model.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private UserRole userRole;
    private Timestamp registeredAt;
    private Timestamp updateedAt;
    private Timestamp deletedAt;

    public static User fromEntity(UserEntity entity) {
        return new User(entity.getId(), entity.getUserName(), entity.getPassword(), entity.getRole(), entity.getRegisteredAt(), entity.getUpdatedAt(), entity.getDeletedAt());
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getUserRole().toString()));
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return this.deletedAt == null;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return this.deletedAt == null;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return this.deletedAt == null;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return this.deletedAt == null;
    }
}
