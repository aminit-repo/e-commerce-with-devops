package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.People;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<People, Long> {
	People findByUsername(String username);
}
