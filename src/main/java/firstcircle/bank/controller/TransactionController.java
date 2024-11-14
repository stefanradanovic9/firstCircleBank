package firstcircle.bank.controller;

import firstcircle.bank.entity.Transaction;
import firstcircle.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        CompletableFuture<Transaction> transactionFuture = transactionService.deposit(accountId, amount);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionFuture.join());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        CompletableFuture<Transaction> transactionFuture = transactionService.withdraw(accountId, amount);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionFuture.join());
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestParam Long senderAccountId,
                                                @RequestParam Long recipientAccountId,
                                                @RequestParam BigDecimal amount) {
        CompletableFuture<Transaction> transactionFuture = transactionService.transfer(senderAccountId, recipientAccountId, amount);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionFuture.join());
    }
}