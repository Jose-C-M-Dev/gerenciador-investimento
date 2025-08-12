package com.spring.gerenciadorinvestimento.service;

import com.spring.gerenciadorinvestimento.controller.dto.CreateStockDto;
import com.spring.gerenciadorinvestimento.entity.Stock;
import com.spring.gerenciadorinvestimento.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDto createStockDto) {
        var stock = new Stock(createStockDto.stockId(), createStockDto.description());
        stockRepository.save(stock);
    }
}
