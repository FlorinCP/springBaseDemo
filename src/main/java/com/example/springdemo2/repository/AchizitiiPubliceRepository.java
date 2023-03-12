package com.example.springdemo2.repository;

import com.example.springdemo2.model.AchizitiiPublice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchizitiiPubliceRepository extends JpaRepository<AchizitiiPublice,Long> {
}
