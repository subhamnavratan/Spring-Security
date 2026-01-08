package com.Subham.PRASAG.config;

import com.Subham.PRASAG.model.User;
import com.Subham.PRASAG.model.Permission;
import com.Subham.PRASAG.model.RolePermissionMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<Permission> permissions =
                RolePermissionMapper.permission(user.getRole());
/*SimpleGrantedAuthority is what Spring Security understands  .  p.name() converts enum → string*/
        return permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
