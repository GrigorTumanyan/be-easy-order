package com.ggit.orderstorage.controller;

import com.ggit.orderstorage.data.dto.auth.AuthenticationRequestDto;
import com.ggit.orderstorage.data.dto.auth.RegisterDto;
import com.ggit.orderstorage.data.dto.user.UserDto;
import com.ggit.orderstorage.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
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
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<UserDto> register(@RequestBody RegisterDto dto) {
		UserDto register = authService.register(dto);
		return new ResponseEntity<>(register, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Void> login(@RequestBody AuthenticationRequestDto dto, HttpServletResponse httpResponse) {
		List<String> tokens = authService.login(dto);
		setTokens(tokens, httpResponse);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("activation/{id}")
	public ResponseEntity<String> activation(@PathVariable Long id) {
		String resp = authService.activateAccount(id);
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}


	private void setTokens(List<String> tokens, HttpServletResponse httpResponse) {
		httpResponse.setHeader("access_token", tokens.get(0));
		httpResponse.setHeader("refresh_token", tokens.get(1));
	}
}
