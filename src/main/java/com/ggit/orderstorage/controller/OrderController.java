package com.ggit.orderstorage.controller;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.ggit.orderstorage.dto.FoodDto;
import com.ggit.orderstorage.dto.OrderDto;
import com.ggit.orderstorage.entity.Food;
import com.ggit.orderstorage.entity.Order;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

	private List<OrderDto> orderDto = new ArrayList<>();

	{
		for (int i = 1; i < 10; i++) {
			var dto = new OrderDto();

			var foodDto = new FoodDto();
			foodDto.setPrice(i);
			foodDto.setFood("A");
			foodDto.setCount(i);
			var foodDto1 = new FoodDto();
			foodDto1.setCount(i * 4);
			foodDto1.setFood("B");
			foodDto1.setPrice(i * 1000);


			dto.setItems(List.of(foodDto1, foodDto));
			dto.setName("John" + i);
			dto.setSurname("Smith" + i);

			orderDto.add(dto);
		}

	}

	@PostMapping()
	public ResponseEntity<Object> add(@RequestBody OrderDto dto) {
		Order order = new Order();
		order.setId(RandomUtil.getPositiveInt());
		order.setName(dto.getName());
		order.setSurName(dto.getSurname());
		if (dto.getItems() != null && !dto.getItems().isEmpty()) {
			order.setFoodList(
				dto.getItems().stream().map(foodDto -> {
					Food food = new Food();
					food.setName(foodDto.getFood());
					food.setCount(foodDto.getCount());
					food.setPrice(foodDto.getPrice());
					return food;
				}).toList());

		}
		orderDto.add(dto);
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}

	@GetMapping()
	public ResponseEntity<List<OrderDto>> get() {

		return new ResponseEntity<>(orderDto, HttpStatus.OK);
	}
}
