package com.spring.gerenciadorinvestimento.service;

import com.spring.gerenciadorinvestimento.client.BrapiClient;
import com.spring.gerenciadorinvestimento.client.dto.BrapiResponseDto;
import com.spring.gerenciadorinvestimento.controller.dto.AccountStockDto;
import com.spring.gerenciadorinvestimento.controller.dto.AccountStockResponseDto;
import com.spring.gerenciadorinvestimento.entity.AccountStock;
import com.spring.gerenciadorinvestimento.entity.AccountStockId;
import com.spring.gerenciadorinvestimento.repository.AccountRepository;
import com.spring.gerenciadorinvestimento.repository.AccountStockRepository;
import com.spring.gerenciadorinvestimento.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Value("#{environment.TOKEN}")
    private String TOKEN;
    private StockRepository stockRepository;
    private AccountRepository accountRepository;
    private AccountStockRepository accountStockRepository;
    private BrapiClient brapiClient;

    public AccountService(StockRepository stockRepository, AccountRepository accountRepository, AccountStockRepository accountStockRepository, BrapiClient brapiClient) {
        this.stockRepository = stockRepository;
        this.accountRepository = accountRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
    }

    public void associateStock(String accountId, AccountStockDto accountStockDto) {

        var account = accountRepository.findById(UUID.fromString(accountId)).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Account nao existe"));

        var stock = stockRepository.findById(accountStockDto.stockId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock nao existe"));

        var id = new AccountStockId(account.getAccountId(), stock.getStockId());

        var accountStockEntity = new AccountStock(id, account, stock, accountStockDto.quantity());

        accountStockRepository.save(accountStockEntity);
    }

    public List<AccountStockResponseDto> listStocks(String accountId) {

        var account = accountRepository.findById(UUID.fromString(accountId)).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        return account.getAccountStocks().stream().map(as ->
                        new AccountStockResponseDto(as.getStock().getStockId(), as.getQuantity(), getTotal(as.getQuantity(), as.getStock().getStockId())))
                .toList();
    }

    private double getTotal(Integer quantity, String stockId) {
        var response = brapiClient.getQuote(TOKEN, stockId);
        var price = response.results().getFirst().regularMarketPrice();
        return quantity * price;
    }
}
