package com.example.spring_security_with_jwt.repository;

import com.example.spring_security_with_jwt.entity.SecretKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtKeyRepository extends JpaRepository<SecretKeyEntity, Long> {
    Optional<SecretKeyEntity> findTopByOrderByCreatedAtDesc();
}
