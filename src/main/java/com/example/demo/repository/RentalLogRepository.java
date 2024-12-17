package com.example.demo.repository;

import com.example.demo.entity.RentalLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalLogRepository extends JpaRepository<RentalLog, Long> {
}
