package com.example.demo.controllers;

import java.util.List;

import com.example.demo.model.persistence.People;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	private static Logger log = LogManager.getLogger(  OrderController.class);
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		People user = userRepository.findByUsername(username);
		if(user == null) {
			log.info("OrderController:submit  failure creating order  for "+username);
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		log.info("OrderController:submit  success creating order for "+username);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		People user = userRepository.findByUsername(username);
		if(user == null) {
			log.info("OrderController:getOrdersForUser  failure fetching orders  for "+username);
			return ResponseEntity.notFound().build();
		}
		log.info("OrderController:getOrdersForUser  success fetching orders for "+username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
