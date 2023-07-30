package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties("rsa")
public record RSAProperties(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
}
