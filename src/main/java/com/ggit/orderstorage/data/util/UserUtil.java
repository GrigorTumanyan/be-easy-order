package com.ggit.orderstorage.data.util;

import com.ggit.orderstorage.data.dto.auth.RegisterDto;
import com.ggit.orderstorage.data.dto.user.UserDto;
import com.ggit.orderstorage.data.user.User;

public class UserUtil {

	private UserUtil() {
	}

	public static User registerDtoToUser(RegisterDto dto) {
		return new User()
		.setEmail(dto.getEmail())
		.setName(dto.getName())
		.setSurname(dto.getSurname())
		.setPassword(dto.getPassword());
	}

	public static UserDto userToUserdto(User user) {
		var userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setName(user.getName());
		userDto.setEmail(user.getEmail());
		return userDto;
	}

}
