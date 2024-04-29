package com.ggit.orderstorage.data.dto;

import java.util.List;

public class OrderDto {

	private String name;
	private String surname;
	private List<FoodDto> items;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<FoodDto> getItems() {
		return items;
	}

	public void setItems(List<FoodDto> items) {
		this.items = items;
	}
}
