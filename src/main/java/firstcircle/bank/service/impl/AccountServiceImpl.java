package firstcircle.bank.service.impl;

import firstcircle.bank.entity.Account;
import firstcircle.bank.exception.AccountNotFoundException;
import firstcircle.bank.exception.InsufficientFundsException;
import firstcircle.bank.repository.AccountRepository;
import firstcircle.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {


    private final AccountRepository accountRepository;


    @Override
    @Transactional
    public Account createAccount(String accountHolder, BigDecimal initialDeposit) {
        Account account = new Account();
        account.setAccountHolder(accountHolder);
        account.setBalance(initialDeposit);
        return accountRepository.save(account);
    }

    @Override
    public Account update(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public BigDecimal getBalance(Long accountId) {
        return findAccountById(accountId).getBalance();
    }

    @Override
    public Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }
}
