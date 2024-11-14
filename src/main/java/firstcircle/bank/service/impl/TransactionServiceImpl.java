package firstcircle.bank.service.impl;

import firstcircle.bank.entity.Account;
import firstcircle.bank.entity.Transaction;
import firstcircle.bank.entity.enums.TransactionStatus;
import firstcircle.bank.entity.enums.TransactionType;
import firstcircle.bank.exception.InsufficientFundsException;
import firstcircle.bank.repository.TransactionRepository;
import firstcircle.bank.service.AccountService;
import firstcircle.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    @Async
    @Override
    public CompletableFuture<Transaction> deposit(Long accountId, BigDecimal amount) {
        return processTransaction(accountId, amount, TransactionType.DEPOSIT);
    }

    @Async
    @Override
    public CompletableFuture<Transaction> withdraw(Long accountId, BigDecimal amount) {
        return processTransactionWithRetry(accountId, amount, TransactionType.WITHDRAWAL);
    }
    @Async
    @Override
    public CompletableFuture<Transaction> transfer(Long senderAccountId, Long recipientAccountId, BigDecimal amount) {
        return processTransfer(senderAccountId, recipientAccountId, amount);
    }

    private CompletableFuture<Transaction> processTransaction(Long accountId, BigDecimal amount, TransactionType type) {
        Transaction transaction = new Transaction();
        transaction.setAccount(accountService.findAccountById(accountId));
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.PENDING);

        Transaction savedTransaction = transactionRepository.save(transaction);

        try {
            Account account = savedTransaction.getAccount();
            if (type == TransactionType.WITHDRAWAL && account.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient balance");
            }

            updateAccountBalance(account, amount, type);
            accountService.update(account);

            updateTransactionStatus(savedTransaction, TransactionStatus.SUCCESS);
        } catch (Exception e) {
            updateTransactionStatus(savedTransaction, TransactionStatus.FAILED);
        }

        return CompletableFuture.completedFuture(savedTransaction);
    }

    private void updateAccountBalance(Account account, BigDecimal amount, TransactionType type) {
        if (type == TransactionType.DEPOSIT) {
            account.setBalance(account.getBalance().add(amount));
        } else if (type == TransactionType.WITHDRAWAL) {
            account.setBalance(account.getBalance().subtract(amount));
        }
    }

    private void updateTransactionStatus(Transaction transaction, TransactionStatus status) {
        transaction.setStatus(status);
        transactionRepository.save(transaction);
    }

    private CompletableFuture<Transaction> processTransactionWithRetry(Long accountId, BigDecimal amount, TransactionType type) {
        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                return processTransaction(accountId, amount, type);
            } catch (OptimisticLockingFailureException e) {
                attempt++;
                if (attempt >= maxRetries) {
                    throw new RuntimeException("Failed to complete transaction after multiple retries");
                }
            }
        }

        throw new RuntimeException("Unexpected error");
    }

    private CompletableFuture<Transaction> processTransfer(Long senderAccountId, Long recipientAccountId, BigDecimal amount) {
        // Process sender withdrawal
        CompletableFuture<Transaction> senderTransactionFuture = processTransactionWithRetry(senderAccountId, amount, TransactionType.WITHDRAWAL);
        senderTransactionFuture.thenAccept(senderTransaction -> {
            if (senderTransaction.getStatus() == TransactionStatus.SUCCESS) {
                // Process recipient deposit only if sender transaction is successful
                processTransaction(recipientAccountId, amount, TransactionType.DEPOSIT);
            } else {
                // If sender withdrawal fails, skip recipient deposit
                System.out.println("Transfer failed: Insufficient funds or error in sender transaction");
            }
        });

        return senderTransactionFuture;
    }
}
