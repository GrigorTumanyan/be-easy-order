package com.ggit.orderstorage.service.impl;

import com.ggit.orderstorage.data.dto.user.UserDto;
import com.ggit.orderstorage.data.user.User;
import com.ggit.orderstorage.exception.UserNotFoundException;
import com.ggit.orderstorage.repository.UserRepository;
import com.ggit.orderstorage.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UserNotFoundException(String.format("User with email: %s not found", email)));
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new UserNotFoundException(String.format("User with id: %s not found", id)));
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	public void saveVoid (User user) {
		userRepository.save(user);
	}
}
