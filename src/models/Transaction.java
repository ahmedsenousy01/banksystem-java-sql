package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

import models.enums.TransactionType;

public class Transaction {
    private Integer id;
    private Integer senderAccountId;
    private Integer recipientAccountId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private Timestamp transactionDate;

    public Transaction(Integer id, Integer senderAccountId, Integer recipientAccountId, BigDecimal amount,
            TransactionType transactionType, Timestamp transactionDate) {
        this.id = id;
        this.senderAccountId = senderAccountId;
        this.recipientAccountId = recipientAccountId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(int senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public Integer getRecipientAccountId() {
        return recipientAccountId;
    }

    public void setRecipientAccountId(int recipientAccountId) {
        this.recipientAccountId = recipientAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", senderAccountId=" + senderAccountId +
                ", recipientAccountId=" + recipientAccountId +
                ", amount=" + amount +
                ", transactionType=" + transactionType.name() +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
