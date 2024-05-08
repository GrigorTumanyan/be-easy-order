package com.ggit.orderstorage.data.dto.user;

import java.util.Objects;

public class UserDto {

	private String name;
	private String surname;
	private String email;

	public UserDto(String name, String surname, String email) {
		this.name = name;
		this.surname = surname;
		this.email = email;
	}

	public UserDto() {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserDto userDto = (UserDto) o;
		return Objects.equals(name, userDto.name) && Objects.equals(surname, userDto.surname)
			&& Objects.equals(email, userDto.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, surname, email);
	}
}
