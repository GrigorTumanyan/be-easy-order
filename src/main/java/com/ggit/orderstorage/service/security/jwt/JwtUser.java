package com.ggit.orderstorage.service.security.jwt;

import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUser implements UserDetails {

	private final Long id;
	private final String name;
	private final String surname;
	private final String email;
	private final String password;
	private final Boolean enabled;
	private final LocalDateTime lastPasswordResetDate;
	private final Collection<? extends GrantedAuthority> authorities;

	public JwtUser(Long id, String name, String surname, String email, String password, Boolean enabled,
		LocalDateTime lastPasswordResetDate, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.lastPasswordResetDate = lastPasswordResetDate;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
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
		return enabled;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getEmail() {
		return email;
	}

	public LocalDateTime getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}
}
