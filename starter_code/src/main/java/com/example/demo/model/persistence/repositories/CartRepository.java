package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.People;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.Cart;

import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(People user);
}
