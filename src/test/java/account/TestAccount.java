package account;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TestAccount {
    @Test
    void withdraw_returnsFalse_whenAmountLessThanZero() {
        TransactionManager transactionManager = new TransactionManager();
        Account beneficiaryAccount = new Account(1, transactionManager);
        Account originatorAccount = new Account(2, transactionManager);
        assertFalse(originatorAccount.withdraw(-100, beneficiaryAccount));
    }

    @Test
    void withdraw_returnsFalse_whenAmountAboveBalance() {
        TransactionManager transactionManager = new TransactionManager();
        Account beneficiaryAccount = new Account(1, transactionManager);
        Account originatorAccount = new Account(2, transactionManager);
        originatorAccount.add(100);
        assertFalse(originatorAccount.withdraw(200, beneficiaryAccount));
    }

    @Test
    void withdraw_returnsTrue_whenAmountSuit() throws InterruptedException {
        TransactionManager transactionManager = new TransactionManager();
        Account beneficiaryAccount = new Account(1, transactionManager);
        Account originatorAccount = new Account(2, transactionManager);
        beneficiaryAccount.add(100);
        TimeUnit.MILLISECONDS.sleep(1);
        assertTrue(beneficiaryAccount.withdraw(50, originatorAccount));
    }

    @Test
    void withdrawCash_returnsFalse_whenAmountLessThanZero() {
        TransactionManager transactionManager = new TransactionManager();
        Account account = new Account(1, transactionManager);
        assertFalse(account.withdrawCash(-200));
    }

    @Test
    void withdrawCash_returnsFalse_whenAmountAboveBalance() {
        TransactionManager transactionManager = new TransactionManager();
        Account account = new Account(1, transactionManager);
        account.add(100);
        assertFalse(account.withdrawCash(200));
    }

    @Test
    void withdrawCash_returnsTrue_whenAmountSuit() throws InterruptedException {
        TransactionManager transactionManager = new TransactionManager();
        Account account = new Account(1, transactionManager);
        account.add(100);
        TimeUnit.MILLISECONDS.sleep(1);
        assertTrue(account.withdrawCash(50));
    }

    @Test
    void add_returnsFalse_whenAmountLessZero() {
        TransactionManager transactionManager = new TransactionManager();
        Account account = new Account(1, transactionManager);
        assertFalse(account.add(-100));
    }

    @Test
    void add_returnsTrue_whenAmountSuit() {
        TransactionManager transactionManager = new TransactionManager();
        Account account = new Account(1, transactionManager);
        assertTrue(account.add(100));
    }

    @Test
    void addCash_returnsFalse_whenAmountLessZero() {
        TransactionManager transactionManager = new TransactionManager();
        Account account = new Account(1, transactionManager);
        assertFalse(account.addCash(-100));
    }

    @Test
    void addCash_returnsTrue_whenAmountSuit() {
        TransactionManager transactionManager = new TransactionManager();
        Account account = new Account(1, transactionManager);
        assertTrue(account.addCash(100));
    }

    @Test
    void balanceOn_shouldReturnCorrectBalanceOnDate() throws InterruptedException {
        TransactionManager transactionManager = new TransactionManager();
        Account account = new Account(1, transactionManager);
        account.addCash(1000);
        account.add(500);
        TimeUnit.MILLISECONDS.sleep(1);
        account.withdrawCash(500);
        account.withdraw(500, new Account(2, transactionManager));
        LocalDateTime currentDateTime1 = LocalDateTime.now().plusNanos(10);
        TimeUnit.MILLISECONDS.sleep(1);
        account.addCash(200);
        account.withdrawCash(100);
        LocalDateTime currentDateTime2 = LocalDateTime.now().plusNanos(10);
        assertEquals(500, account.balanceOn(currentDateTime1));
        assertEquals(600, account.balanceOn(currentDateTime2));
    }
}
