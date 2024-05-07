package com.ggit.orderstorage.data.dto.auth;

import java.util.Objects;

public class RegisterDto {
	private String name;
	private String surname;
	private String email;
	private String password;

	public RegisterDto(String name, String surname, String email, String password) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RegisterDto registerDto = (RegisterDto) o;
		return Objects.equals(name, registerDto.name) && Objects.equals(surname, registerDto.surname)
			&& Objects.equals(email, registerDto.email) && Objects.equals(password, registerDto.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, surname, email, password);
	}

	@Override
	public String toString() {
		return "UserDto{" +
			"name='" + name + '\'' +
			", surname='" + surname + '\'' +
			", email='" + email + '\'' +
			", password='" + password + '\'' +
			'}';
	}
}
