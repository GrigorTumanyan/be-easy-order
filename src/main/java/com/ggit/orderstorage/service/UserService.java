package com.ggit.orderstorage.service;

import com.ggit.orderstorage.data.user.User;

public interface UserService {

	User findByEmail(String email);

	User findById(Long id);

	User save(User user);
}
