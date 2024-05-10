package com.ggit.orderstorage.service;

import com.ggit.orderstorage.data.dto.auth.AuthenticationRequestDto;
import com.ggit.orderstorage.data.dto.auth.RegisterDto;
import com.ggit.orderstorage.data.dto.user.UserDto;

public interface AuthService {

	UserDto register(RegisterDto dto);

	void login(AuthenticationRequestDto dto);

	String activateAccount(Long id);
}
