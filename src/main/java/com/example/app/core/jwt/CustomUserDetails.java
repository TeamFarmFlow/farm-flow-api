package com.example.app.core.jwt;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.app.user.domain.enums.UserType;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {
  private final Long id;
  private final String email;
  private final UserType type;
  private final Collection<? extends GrantedAuthority> authorities;

  public CustomUserDetails(JwtClaim jwtClaim) {
    this.id = jwtClaim.getId();
    this.email = jwtClaim.getEmail();
    this.type = jwtClaim.getType();
    this.authorities = List.of();
  }

  public Long getId() {
    return id;
  }

  public UserType getType() {
    return type;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return null;
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
