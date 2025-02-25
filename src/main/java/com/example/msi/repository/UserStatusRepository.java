package com.example.msi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.msi.entities.UserStatus;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
} 