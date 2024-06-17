package com.ggit.orderstorage.service.impl;

import com.ggit.orderstorage.data.dto.auth.AuthenticationRequestDto;
import com.ggit.orderstorage.data.dto.auth.RegisterDto;
import com.ggit.orderstorage.data.dto.user.UserDto;
import com.ggit.orderstorage.data.user.User;
import com.ggit.orderstorage.data.user.UserType;
import com.ggit.orderstorage.data.util.UserUtil;
import com.ggit.orderstorage.exception.ConflictException;
import com.ggit.orderstorage.security.jwt.JwtTokenProvider;
import com.ggit.orderstorage.service.AuthService;
import com.ggit.orderstorage.service.MailService;
import com.ggit.orderstorage.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final BCryptPasswordEncoder passwordEncoder;
	private final MailService mailService;
	private final UserService userService;


	public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
		BCryptPasswordEncoder passwordEncoder,
		MailService mailService,
		UserService userService) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.passwordEncoder = passwordEncoder;
		this.mailService = mailService;
		this.userService = userService;
	}

	@Override
	public UserDto register(RegisterDto dto) {
		var now = LocalDateTime.now();
		dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		var user = UserUtil.registerDtoToUser(dto);
		user.setCreatedDate(now)
			.setLastModifiedDate(now)
			.setActive(false)
			.setUserType(UserType.USER);
		User savedUser = userService.save(user);
		mailService.sendMail(user.getEmail(), "Account activation", generateActivationLink(savedUser.getId()));
		return UserUtil.userToUserdto(savedUser);
	}


	@Override
	public UserDto login(AuthenticationRequestDto dto, HttpServletResponse httpResponse) {
		var email = dto.getEmail();
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, dto.getPassword()));
		User user = userService.findByEmail(dto.getEmail());
		List<String> tokens = createTokens(email, user.getUserType());
		setTokens(tokens, httpResponse);
		user.setRefreshToken(tokens.get(1))
			.setLastModifiedDate(LocalDateTime.now());
		userService.save(user);
		return UserUtil.userToUserdto(user);
	}

	@Override
	public String activateAccount(Long id) {
		User user = userService.findById(id);
		if (Boolean.TRUE.equals(user.getActive())) {
			throw new ConflictException(String.format("User with id: %s has already activated", id));
		}
		user.setLastModifiedDate(LocalDateTime.now())
			.setActive(true);
		userService.save(user);
		return "User successfully activated ";
	}

	/**
	 * @param email    user's email which will serves for creating appropriate token
	 * @param userType user's type which serves for correct authentication
	 * @return 2 Strings. The first string is access token, the second is refresh token
	 */

	private List<String> createTokens(String email, UserType userType) {
		return jwtTokenProvider.createTokens(email, userType);
	}

	private String generateActivationLink(Long id) {
		String link = "http://localhost:8080/auth/activation/" + id;
		return "Please click on this link for activating your account \n" + link;
	}

	private void setTokens(List<String> tokens, HttpServletResponse httpResponse) {
		httpResponse.setHeader("access_token", tokens.get(0));
		httpResponse.setHeader("refresh_token", tokens.get(1));
	}
}
