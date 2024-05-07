package com.ggit.orderstorage.controller;

import com.ggit.orderstorage.data.dto.auth.AuthenticationRequestDto;
import com.ggit.orderstorage.data.dto.auth.RegisterDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

	@PostMapping("/register")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<String> register(@RequestBody RegisterDto dto) {
		System.out.println(dto);
		return new ResponseEntity<>("ok", HttpStatus.CREATED);
	}

	@PostMapping("/login")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<String> login(@RequestBody AuthenticationRequestDto dto) {
		System.out.println(dto);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
}
