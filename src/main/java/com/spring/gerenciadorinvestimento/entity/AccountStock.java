package com.spring.gerenciadorinvestimento.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_accounts_stocks")
public class AccountStock {

    @EmbeddedId
    private AccountStockId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("stockId")
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public AccountStock() {
    }

    public AccountStock(Account account, Stock stock, Integer quantity) {
        this.account = account;
        this.stock = stock;
        this.quantity = quantity;
        this.id = new AccountStockId(account.getAccountId(), stock.getStockId());
    }

    public AccountStock(AccountStockId id, Account account, Stock stock, Integer quantity) {
        this.id = id;
        this.account = account;
        this.stock = stock;
        this.quantity = quantity;
    }

    public AccountStockId getId() {
        return id;
    }

    public void setId(AccountStockId id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        if (account != null && this.stock != null) {
            this.id = new AccountStockId(account.getAccountId(), this.stock.getStockId());
        }
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
        if (this.account != null && stock != null) {
            this.id = new AccountStockId(this.account.getAccountId(), stock.getStockId());
        }
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountStock)) return false;
        AccountStock that = (AccountStock) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AccountStock{" +
                "id=" + id +
                ", quantity=" + quantity +
                '}';
    }
}