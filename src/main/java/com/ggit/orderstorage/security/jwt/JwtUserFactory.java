package com.ggit.orderstorage.security.jwt;

import com.ggit.orderstorage.data.user.User;
import com.ggit.orderstorage.data.user.UserType;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtUserFactory {

	private JwtUserFactory(){}

	public static JwtUser create(User user) {
		return new JwtUser(
			user.getId(),
			user.getName(),
			user.getSurname(),
			user.getEmail(),
			user.getPassword(),
			Boolean.TRUE.equals(user.getActive()),
			user.getLastModifiedDate(),
			createGrantedAuthorityList(user.getUserType()));
	}

	private static List<GrantedAuthority> createGrantedAuthorityList(UserType userType) {
		return Collections.singletonList(new SimpleGrantedAuthority(userType.name()));
	}

}
