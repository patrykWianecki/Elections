package com.app.repository;

import com.app.model.Constituency;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstituencyRepository extends JpaRepository<Constituency, Long> {
}
