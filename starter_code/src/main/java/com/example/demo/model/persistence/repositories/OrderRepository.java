package com.example.demo.model.persistence.repositories;

import java.util.List;

import com.example.demo.model.persistence.People;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.UserOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(People user);
}
