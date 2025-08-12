package com.spring.gerenciadorinvestimento.repository;

import com.spring.gerenciadorinvestimento.entity.AccountStock;
import com.spring.gerenciadorinvestimento.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
