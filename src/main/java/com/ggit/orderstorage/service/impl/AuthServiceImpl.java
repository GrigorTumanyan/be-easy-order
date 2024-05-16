package com.ggit.orderstorage.service.impl;

import com.ggit.orderstorage.data.dto.auth.AuthenticationRequestDto;
import com.ggit.orderstorage.data.dto.auth.RegisterDto;
import com.ggit.orderstorage.data.dto.user.UserDto;
import com.ggit.orderstorage.data.user.User;
import com.ggit.orderstorage.data.user.UserType;
import com.ggit.orderstorage.exception.ConflictException;
import com.ggit.orderstorage.exception.UserNotFoundException;
import com.ggit.orderstorage.repository.UserRepository;
import com.ggit.orderstorage.service.AuthService;
import com.ggit.orderstorage.service.MailService;
import com.ggit.orderstorage.service.util.UserUtil;
import java.time.LocalDateTime;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

	private final BCryptPasswordEncoder passwordEncoder;
	private final MailService mailService;
	private final UserRepository userRepository;


	public AuthServiceImpl(BCryptPasswordEncoder passwordEncoder, MailService mailService,
		UserRepository userRepository) {
		this.passwordEncoder = passwordEncoder;
		this.mailService = mailService;
		this.userRepository = userRepository;
	}

	@Override
	public UserDto register(RegisterDto dto) {
		var now = LocalDateTime.now();
		dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		var user = UserUtil.registerDtoToUser(dto);
		user.setCreatedDate(now)
			.setLastModifiedDate(now)
			.setUserType(UserType.USER);
		User savedUser = userRepository.save(user);
		mailService.sendMail(user.getEmail(), "Account activation", generateActivationLink(savedUser.getId()));
		return UserUtil.userToUserdto(savedUser);
	}


	@Override
	public void login(AuthenticationRequestDto dto) {

	}

	@Override
	public String activateAccount(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new UserNotFoundException(String.format("User with id: %s not found", id)));
		if (Boolean.TRUE.equals(user.getActive())) {
			throw new ConflictException(String.format("User with id: %s has already activated", id));
		}
		user.setActive(true);
		userRepository.save(user);
		return "User successfully activated ";
	}

	private String generateActivationLink(Long id) {
		String link = "http://localhost:8080/auth/activation/" + id;
		return "Please click on this link for activating your account \n" + link;
	}


}
