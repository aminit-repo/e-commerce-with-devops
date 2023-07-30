package com.example.demo.security;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.People;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DefaultAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private static Logger log = LogManager.getLogger(DefaultAuthenticationProvider.class);
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        //get users credentials
        String username= authentication.getName();
        String password= authentication.getCredentials().toString();

        System.out.print(authentication.getName());
        //get the users credentials
        People user = userRepository.findByUsername(username);
        if(user!= null){
            if(passwordEncoder.matches(password,user.getPassword())){
                return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
            }else{
                log.error("DefaultAuthenticationProvider:authenticate failure authenticating  Credentails on user "+username);
                throw new BadCredentialsException("incorrect password");
            }
        }
        log.error("DefaultAuthenticationProvider:authenticate failure authenticating username with "+username);
        throw new UsernameNotFoundException("User does not exist");
    }


    @Override
    public boolean supports(Class<?> authentication){
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
