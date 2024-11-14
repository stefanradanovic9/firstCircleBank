package firstcircle.bank.service;

import firstcircle.bank.entity.Account;

import java.math.BigDecimal;

public interface AccountService {

     Account createAccount(String accountHolder, BigDecimal initialDeposit);

    Account update(Account account);
    BigDecimal getBalance(Long accountId);
    Account findAccountById(Long accountId);
}
