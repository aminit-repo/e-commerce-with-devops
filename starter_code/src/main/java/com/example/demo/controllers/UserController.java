package com.example.demo.controllers;

import com.example.demo.model.persistence.People;
import com.example.demo.security.JWTService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private JWTService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	private static Logger log = LogManager.getLogger( UserController.class);

	@GetMapping("/id/{id}")
	public ResponseEntity<People> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<People> findByUserName(@PathVariable String username) {
		People user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<People> createUser(@RequestBody CreateUserRequest createUserRequest) {

		if(createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			if(createUserRequest.getPassword().length() > 7){
				try{
					Cart cart = new Cart();
					cartRepository.save(cart);
					People people=new People(createUserRequest.getUsername(),cart, passwordEncoder.encode(createUserRequest.getPassword()));
					userRepository.save(people);
					log.info("UserController:createUser  success creating user "+people.getUsername());
					return ResponseEntity.ok(people);
				}catch (Exception e){
					log.info("UserController:createUser  failure creating user "+createUserRequest.getUsername());
				}

			}
		}
		log.info("UserController:createUser  failure creating user "+createUserRequest.getUsername());
			return ResponseEntity.internalServerError().build();
	}

	@PostMapping("/login")
	public ResponseEntity<String> signin(@RequestBody CreateUserRequest createUserRequest){
		Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(createUserRequest.getUsername(), createUserRequest.getPassword()));
		log.info("UserController:signin  success signin user "+createUserRequest.getUsername());
		return  ResponseEntity.ok().header("Authorization",jwtService.getJWTString(authentication)).build();
	}
	
}
