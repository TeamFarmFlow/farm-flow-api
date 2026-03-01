package com.example.app.farm.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmUserRepository extends JpaRepository<FarmUser, FarmUserId> {}
