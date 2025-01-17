package com.example.spring_security_with_jwt.service;

import com.example.spring_security_with_jwt.entity.SecretKeyEntity;
import com.example.spring_security_with_jwt.repository.JwtKeyRepository;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class JwtKeyService {
    private final JwtKeyRepository jwtKeyRepository;
    private SecretKey cachedKey;

    public JwtKeyService(JwtKeyRepository jwtKeyRepository) {
        this.jwtKeyRepository = jwtKeyRepository;
    }

    @PostConstruct
    public void init() {
        loadSecretKey();
    }

    private void loadSecretKey() {
        String latestKey = getLatestKey();
        byte[] decodedKey = Base64.getDecoder().decode(latestKey);
        cachedKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public String getLatestKey() {
        Optional<SecretKeyEntity> optionalSecretKeyEntity = jwtKeyRepository.findTopByOrderByCreatedAtDesc();
        if (optionalSecretKeyEntity.isPresent()) {
            SecretKeyEntity secretKeyEntity = optionalSecretKeyEntity.get();
            return secretKeyEntity.getSecretKey();
        } else {
            return generateSecretKeyAndSaveItToDB();
        }
    }

    public SecretKey getCachedKey() {
        if (cachedKey == null) {
            loadSecretKey();
        }
        return cachedKey;
    }

    public String generateSecretKeyAndSaveItToDB() {
        String secretKey = generateSecretKey();

        SecretKeyEntity secretKeyEntity = SecretKeyEntity.builder().secretKey(secretKey).createdAt(LocalDateTime.now()).build();
        jwtKeyRepository.save(secretKeyEntity);
        loadSecretKey();
        return secretKey;
    }

    public String generateSecretKey() {
        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("HmacSHA256");

            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error instantiating Key generator object!");
        }
    }
}
