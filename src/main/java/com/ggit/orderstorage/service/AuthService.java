package com.ggit.orderstorage.service;

import com.ggit.orderstorage.data.dto.auth.AuthenticationRequestDto;
import com.ggit.orderstorage.data.dto.auth.RegisterDto;
import com.ggit.orderstorage.data.dto.user.UserDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

	UserDto register(RegisterDto dto);

	UserDto login(AuthenticationRequestDto dto, HttpServletResponse httpResponse);

	String activateAccount(Long id);
}
