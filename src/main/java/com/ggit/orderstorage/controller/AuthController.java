package com.ggit.orderstorage.controller;

import com.ggit.orderstorage.data.dto.auth.AuthenticationRequestDto;
import com.ggit.orderstorage.data.dto.auth.RegisterDto;
import com.ggit.orderstorage.data.dto.user.UserDto;
import com.ggit.orderstorage.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<UserDto> register(@RequestBody RegisterDto dto) {
		UserDto register = authService.register(dto);
		return new ResponseEntity<>(register, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<UserDto> login(@RequestBody AuthenticationRequestDto dto, HttpServletResponse httpResponse) {
		var userDto = authService.login(dto, httpResponse);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	@GetMapping("activation/{id}")
	public ResponseEntity<String> activation(@PathVariable Long id) {
		String resp = authService.activateAccount(id);
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}
}
