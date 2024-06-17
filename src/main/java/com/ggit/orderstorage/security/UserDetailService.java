package com.ggit.orderstorage.security;

import com.ggit.orderstorage.data.user.User;
import com.ggit.orderstorage.exception.UserNotFoundException;
import com.ggit.orderstorage.repository.UserRepository;
import com.ggit.orderstorage.security.jwt.JwtUserFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username)
			.orElseThrow(() -> new UserNotFoundException("User with email: %s not found"));
		return JwtUserFactory.create(user);
	}
}
