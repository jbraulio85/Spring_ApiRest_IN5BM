package com.in5bm.soccerfieldmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in5bm.soccerfieldmanager.models.User;

public interface AuthRepository extends  JpaRepository<User, String> {
    User findByEmail(String email);
}
