package com.ggit.orderstorage.service.util;

import com.ggit.orderstorage.data.dto.auth.RegisterDto;
import com.ggit.orderstorage.data.dto.user.UserDto;
import com.ggit.orderstorage.data.user.User;

public class UserUtil {

	private UserUtil() {
	}

	public static User registerDtoToUser(RegisterDto dto) {
		var user = new User();
		user.setEmail(dto.getEmail());
		user.setName(dto.getName());
		user.setSurname(dto.getSurname());
		user.setPassword(dto.getPassword());
		return user;
	}

	public static UserDto UserToUserdto(User user) {
		var userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setName(user.getName());
		userDto.setEmail(user.getEmail());
		return userDto;
	}

}
