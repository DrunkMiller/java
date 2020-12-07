package account;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TestAccount {

    private LocalDateTime systemDataTimeForTesting = LocalDateTime.now();
    private int minutesToAdd = 0;

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
    void withdraw_returnsTrue_whenAmountSuit()  {
        TransactionManager transactionManager = new TransactionManager();
        Account beneficiaryAccount = new Account(1, transactionManager);
        Account originatorAccount = new Account(2, transactionManager);
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenAnswer(inv -> systemDataTimeForTesting.plusMinutes(++minutesToAdd));
            beneficiaryAccount.add(100);
            assertTrue(beneficiaryAccount.withdraw(50, originatorAccount));
        }
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
    void withdrawCash_returnsTrue_whenAmountSuit() {
        TransactionManager transactionManager = new TransactionManager();
        Account account = new Account(1, transactionManager);
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenAnswer(inv -> systemDataTimeForTesting.plusMinutes(++minutesToAdd));
            account.add(100);
            assertTrue(account.withdrawCash(50));
        }
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
    void balanceOn_shouldReturnCorrectBalanceOnDate() {
        TransactionManager transactionManager = new TransactionManager();
        Account account = new Account(1, transactionManager);

        try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenAnswer(inv -> systemDataTimeForTesting.plusMinutes(++minutesToAdd));
            account.addCash(1000);
            account.add(500);

            account.withdrawCash(500);
            account.withdraw(500, new Account(2, transactionManager));
            LocalDateTime currentDateTime1 = LocalDateTime.now();

            account.addCash(200);
            account.withdrawCash(100);
            LocalDateTime currentDateTime2 = LocalDateTime.now();
            assertEquals(500, account.balanceOn(currentDateTime1));
            assertEquals(600, account.balanceOn(currentDateTime2));
        }
    }
}
