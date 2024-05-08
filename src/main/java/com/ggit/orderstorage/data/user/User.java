package com.ggit.orderstorage.data.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String surname;
	private String email;
	private String password;
	private UserType userType;
	private Boolean active;
	private String refreshToken;
	private String activationForgottenPassword;
	//	@CreatedDate
	private LocalDateTime createdDate;
	//	@LastModifiedDate
	private LocalDateTime lastModifiedDate;


	public User() {
	}

	public User setId(Long id) {
		if (this.id == null) {
			this.id = id;
			return this;
		}
		throw new RuntimeException("You can not change id");
	}

	public User setEmail(String email) {
		if (this.email == null) {
			this.email = email;
			return this;
		}
		throw new RuntimeException("You can not change email");
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public User setName(String name) {
		this.name = name;
		return this;
	}

	public String getSurname() {
		return surname;
	}

	public User setSurname(String surname) {
		this.surname = surname;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public User setPassword(String password) {
		this.password = password;
		return this;
	}

	public UserType getUserType() {
		return userType;
	}

	public User setUserType(UserType userType) {
		this.userType = userType;
		return this;
	}

	public Boolean getActive() {
		return active;
	}

	public User setActive(Boolean active) {
		this.active = active;
		return this;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public User setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}

	public String getActivationForgottenPassword() {
		return activationForgottenPassword;
	}

	public User setActivationForgottenPassword(String activationForgottenPassword) {
		this.activationForgottenPassword = activationForgottenPassword;
		return this;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public User setCreatedDate(LocalDateTime createdDate) {
		if (this.createdDate == null) {
			this.createdDate = createdDate;
			return this;
		}
		throw new RuntimeException("You can not change created date");
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public User setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
		return this;
	}
}
