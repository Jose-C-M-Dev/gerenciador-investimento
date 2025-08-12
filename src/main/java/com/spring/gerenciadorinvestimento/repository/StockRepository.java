package com.spring.gerenciadorinvestimento.repository;

import com.spring.gerenciadorinvestimento.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StockRepository extends JpaRepository<Stock, String> {
}