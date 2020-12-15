package analytics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import accounts.Account;
import accounts.BonusAccount;
import accounts.DebitCard;
import org.junit.jupiter.api.Test;
import storage.TransactionKeyExtractor;
import transactions.Transaction;
import transactions.TransactionManager;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class TestAnalyticsManager {

    private LocalDateTime systemDataTimeForTesting = LocalDateTime.now();
    private int minutesToAdd = 0;

    @Test
    void mostFrequentBeneficiaryOfAccount_shouldReturnCorrectBeneficiary() throws InterruptedException {
        TransactionManager transactionManager = new TransactionManager();
        AnalyticsManager analyticsManager = new AnalyticsManager(transactionManager);
        DebitCard account1 = new DebitCard(1, transactionManager, new BonusAccount(0));
        DebitCard account2 = new DebitCard(2, transactionManager, new BonusAccount(0));
        DebitCard account3 = new DebitCard(3, transactionManager, new BonusAccount(0));

        try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenAnswer(inv -> systemDataTimeForTesting.plusMinutes(++minutesToAdd));

            account1.add(10_000);
            account1.withdraw(1_000, account2);
            account1.withdraw(1_000, account2);
            account1.withdraw(1_000, account3);
            Account actual = analyticsManager.mostFrequentBeneficiaryOfAccount(account1);
            assertEquals(account2, actual);
        }
    }

    @Test
    void topTenExpensivePurchases_shouldReturnCorrectTopPurchases() {
        List<Transaction> transactions = new ArrayList<>();
        TransactionManager transactionManager = mock(TransactionManager.class);
        when(transactionManager.findAllTransactionsByAccount(null)).thenReturn(transactions);
        AnalyticsManager analyticsManager = new AnalyticsManager(transactionManager);
        for (int amount : List.of(1_456, 1_000, 911, 870, 150)) {
            transactions.add(new Transaction(1, amount, null, null));
        }
        Collection<Transaction> actualTransactions = analyticsManager.topTenExpensivePurchases(null);
        assertIterableEquals(transactions, actualTransactions);
    }

    @Test
    void overallBalanceOfAccounts_shouldReturnCorrectBalanceOfAccountsWithoutBonusAccounts() throws InterruptedException {
        TransactionManager transactionManager = new TransactionManager();
        AnalyticsManager analyticsManager = new AnalyticsManager(transactionManager);
        DebitCard account1 = new DebitCard(1, transactionManager, null);
        DebitCard account2 = new DebitCard(2, transactionManager, null);
        DebitCard account3 = new DebitCard(3, transactionManager, null);

        try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenAnswer(inv -> systemDataTimeForTesting.plusMinutes(++minutesToAdd));

            account1.addCash(1_000);
            account2.addCash(200);
            account3.addCash(300);

            double expected = 1_000 + 200 + 300;
            double actual = analyticsManager.overallBalanceOfAccounts(Arrays.asList(account1, account2, account3));
            assertEquals(expected, actual);
        }
    }

    @Test
    void overallBalanceOfAccounts_shouldReturnCorrectBalanceOfAccountsWithBonusAccounts() throws InterruptedException {
        TransactionManager transactionManager = new TransactionManager();
        AnalyticsManager analyticsManager = new AnalyticsManager(transactionManager);
        DebitCard account1 = new DebitCard(1, transactionManager, new BonusAccount(0.11));
        DebitCard account2 = new DebitCard(2, transactionManager, new BonusAccount(0.05));
        DebitCard account3 = new DebitCard(3, transactionManager, new BonusAccount(0.07));

        try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenAnswer(inv -> systemDataTimeForTesting.plusMinutes(++minutesToAdd));

            account1.addCash(1000);
            account2.addCash(500);
            account1.withdraw(100, account3);
            account2.withdraw(200 ,account3);

            double balanceOnAccount1 = 1_000 - 100 + 100 * 0.11;
            double balanceOnAccount2 = 500 - 200 + 200 * 0.05;
            double balanceOnAccount3 = 100 + 200;
            double expected = balanceOnAccount1 + balanceOnAccount2 + balanceOnAccount3;
            double actual = analyticsManager.overallBalanceOfAccounts(Arrays.asList(account1, account2, account3));
            assertEquals(expected, actual);
        }


    }

    @Test
    void uniqueKeysOf_shouldReturnCorrectUniqueKeysOfTransactions() {
        TransactionManager transactionManager = new TransactionManager();
        AnalyticsManager analyticsManager = new AnalyticsManager(transactionManager);
        TransactionKeyExtractor extractor = new TransactionKeyExtractor();
        Transaction transaction1 = new Transaction(1, 100, null, null);
        Transaction transaction2 = new Transaction(2, 200, null, null);
        Collection<Long> expected = Arrays.asList(
                extractor.extract(transaction1),
                extractor.extract(transaction2)
        );
        Set<Long> actual = analyticsManager.uniqueKeysOf(
                Arrays.asList(transaction1, transaction1, transaction2),
                extractor
        );
        assertIterableEquals(expected, actual);
    }

    @Test
    void accountsRangeFrom_shouldReturnCorrectAccountsRange() throws InterruptedException {
        TransactionManager transactionManager = new TransactionManager();
        AnalyticsManager analyticsManager = new AnalyticsManager(transactionManager);
        DebitCard account1 = new DebitCard(1, transactionManager, new BonusAccount(0.11));
        DebitCard account2 = new DebitCard(2, transactionManager, new BonusAccount(0.05));
        DebitCard account3 = new DebitCard(3, transactionManager, new BonusAccount(0.07));
        DebitCard account4 = new DebitCard(4, transactionManager, new BonusAccount(0.07));

        try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenAnswer(inv -> systemDataTimeForTesting.plusMinutes(++minutesToAdd));

            account1.addCash(1000);
            account2.addCash(800);
            account3.addCash(500);
            account4.addCash(200);

            List<Account> expected = Arrays.asList(account3, account2, account1);
            List<Account> actual = analyticsManager.accountsRangeFrom(
                    Arrays.asList(account1, account2, account3, account4),
                    account3,
                    new Comparator<Account>() {
                        @Override
                        public int compare(Account a1, Account a2) {
                            LocalDateTime dateTime = LocalDateTime.now();
                            return (int) (a1.balanceOn(dateTime) - a2.balanceOn(dateTime));
                        }
                    }
            );
            assertIterableEquals(expected, actual);
        }
    }

}
