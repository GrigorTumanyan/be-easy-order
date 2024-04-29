package com.ggit.orderstorage.data.user;

public enum UserType {
	ADMIN("admin", 1), MANAGER("manager", 2), USER("user", 3);

	private final int id;
	private final String name;


	UserType(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
