package firstcircle.bank.service;

import firstcircle.bank.entity.Transaction;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public interface TransactionService {
    CompletableFuture<Transaction> deposit(Long accountId, BigDecimal amount);
    CompletableFuture<Transaction> withdraw(Long accountId, BigDecimal amount);
    CompletableFuture<Transaction> transfer(Long senderAccountId, Long recipientAccountId, BigDecimal amount);
}
