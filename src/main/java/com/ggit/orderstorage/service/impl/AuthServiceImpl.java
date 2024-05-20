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
import com.ggit.orderstorage.service.security.jwt.JwtTokenProvider;
import com.ggit.orderstorage.service.util.UserUtil;
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
	private final UserRepository userRepository;


	public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
		BCryptPasswordEncoder passwordEncoder,
		MailService mailService,
		UserRepository userRepository) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
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
			.setActive(false)
			.setUserType(UserType.USER);
		User savedUser = userRepository.save(user);
		mailService.sendMail(user.getEmail(), "Account activation", generateActivationLink(savedUser.getId()));
		return UserUtil.userToUserdto(savedUser);
	}


	@Override
	public List<String> login(AuthenticationRequestDto dto) {
		var email = dto.getEmail();
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, dto.getPassword()));
		User user = userRepository.findByEmail(dto.getEmail())
			.orElseThrow(() -> new UserNotFoundException("User with email: %s not found"));
		List<String> tokens = createTokens(email, user.getUserType());
		user.setRefreshToken(tokens.get(1))
			.setLastModifiedDate(LocalDateTime.now());
		userRepository.save(user);
		return tokens;
	}

	/**
	 *
	 * @param email user's email which will serves for creating appropriate token
	 * @param userType user's type which serves for correct authentication
	 * @return 2 Strings. The first string is access token, the second is refresh token
	 */

	private List<String> createTokens(String email, UserType userType) {
		return jwtTokenProvider.createTokens(email, userType);
	}

	@Override
	public String activateAccount(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new UserNotFoundException(String.format("User with id: %s not found", id)));
		if (Boolean.TRUE.equals(user.getActive())) {
			throw new ConflictException(String.format("User with id: %s has already activated", id));
		}
		user.setLastModifiedDate(LocalDateTime.now())
			.setActive(true);
		userRepository.save(user);
		return "User successfully activated ";
	}

	private String generateActivationLink(Long id) {
		String link = "http://localhost:8080/auth/activation/" + id;
		return "Please click on this link for activating your account \n" + link;
	}


}
