package analytics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import accounts.Account;
import accounts.BonusAccount;
import accounts.DebitCard;
import accounts.Entry;
import org.junit.jupiter.api.Test;
import storage.KeyExtractor;
import storage.TransactionKeyExtractor;
import transactions.Transaction;
import transactions.TransactionManager;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class TestAnalyticsManager {
    @Test
    void mostFrequentBeneficiaryOfAccount_shouldReturnCorrectBeneficiary() throws InterruptedException {
        TransactionManager transactionManager = new TransactionManager();
        AnalyticsManager analyticsManager = new AnalyticsManager(transactionManager);
        DebitCard account1 = new DebitCard(1, transactionManager, new BonusAccount(0));
        DebitCard account2 = new DebitCard(2, transactionManager, new BonusAccount(0));
        DebitCard account3 = new DebitCard(3, transactionManager, new BonusAccount(0));
        account1.add(10_000);
        TimeUnit.MILLISECONDS.sleep(1);
        account1.withdraw(1_000, account2);
        account1.withdraw(2_000, account2);
        account1.withdraw(3_000, account3);
        DebitCard actual = analyticsManager.mostFrequentBeneficiaryOfAccount(account1);
        assertEquals(account2, actual);
        account1.withdraw(1_000, account3);
        account1.withdraw(1_000, account3);
        actual = analyticsManager.mostFrequentBeneficiaryOfAccount(account1);
        assertEquals(account3, actual);
    }

    @Test
    void topTenExpensivePurchases_shouldReturnCorrectTopPurchases() {
        TransactionManager transactionManager = mock(TransactionManager.class);
        AnalyticsManager analyticsManager = new AnalyticsManager(transactionManager);
        int[] amounts = new int[]{1_000 , 1_456 , 870, 150, 911};
        List<Transaction> transactions = new ArrayList<Transaction>();
        when(transactionManager.findAllTransactionsByAccount(null)).thenReturn(transactions);
        for (int amount : amounts) {
            transactions.add(new Transaction(1, amount, null, null));
        }
        Collection<Transaction> actualTransactions = analyticsManager.topTenExpensivePurchases(null);
        transactions.sort(new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return Double.compare(t2.getAmount(), t1.getAmount());
            }
        });
        assertIterableEquals(transactions, actualTransactions);
    }

    @Test
    void overallBalanceOfAccounts_shouldReturnCorrectBalanceOfAccountsWithoutBonusAccounts() throws InterruptedException {
        TransactionManager transactionManager = new TransactionManager();
        AnalyticsManager analyticsManager = new AnalyticsManager(transactionManager);
        DebitCard account1 = new DebitCard(1, transactionManager, null);
        account1.addCash(1_000);
        DebitCard account2 = new DebitCard(2, transactionManager, null);
        account2.addCash(200);
        DebitCard account3 = new DebitCard(3, transactionManager, null);
        account3.addCash(300);
        TimeUnit.MILLISECONDS.sleep(1);
        double expected = 1_000 + 200 + 300;
        double actual = analyticsManager.overallBalanceOfAccounts(Arrays.asList(account1, account2, account3));
        assertEquals(expected, actual);
    }

    @Test
    void overallBalanceOfAccounts_shouldReturnCorrectBalanceOfAccountsWithBonusAccounts() throws InterruptedException {
        TransactionManager transactionManager = new TransactionManager();
        AnalyticsManager analyticsManager = new AnalyticsManager(transactionManager);
        DebitCard account1 = new DebitCard(1, transactionManager, new BonusAccount(0.11));
        account1.addCash(1000);
        DebitCard account2 = new DebitCard(2, transactionManager, new BonusAccount(0.05));
        account2.addCash(500);
        DebitCard account3 = new DebitCard(3, transactionManager, new BonusAccount(0.07));
        TimeUnit.MILLISECONDS.sleep(1);
        account1.withdraw(100, account3);
        account2.withdraw(200 ,account3);
        TimeUnit.MILLISECONDS.sleep(1);
        double balanceOnAccount1 = 1_000 - 100 + 100 * 0.11;
        double balanceOnAccount2 = 500 - 200 + 200 * 0.05;
        double balanceOnAccount3 = 100 + 200;
        double expected = balanceOnAccount1 + balanceOnAccount2 + balanceOnAccount3;
        double actual = analyticsManager.overallBalanceOfAccounts(Arrays.asList(account1, account2, account3));
        assertEquals(expected, actual);
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
        account1.addCash(1000);
        DebitCard account2 = new DebitCard(2, transactionManager, new BonusAccount(0.05));
        account2.addCash(800);
        DebitCard account3 = new DebitCard(3, transactionManager, new BonusAccount(0.07));
        account3.addCash(500);
        DebitCard account4 = new DebitCard(4, transactionManager, new BonusAccount(0.07));
        account3.addCash(200);
        TimeUnit.MILLISECONDS.sleep(1);
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

    @Test
    void maxExpenseAmountEntryWithinInterval() throws InterruptedException {
        TransactionManager transactionManager = new TransactionManager();
        AnalyticsManager analyticsManager = new AnalyticsManager(transactionManager);
        DebitCard account1 = new DebitCard(1, transactionManager, null);
        account1.addCash(10_000);
        DebitCard account2 = new DebitCard(2, transactionManager, null);
        account2.addCash(10_000);
        LocalDateTime timeStart = LocalDateTime.now();
        TimeUnit.MILLISECONDS.sleep(1);
        account1.withdraw(1_000, account2);
        account1.withdraw(3_000, account2);
        account1.withdraw(500, account2);
        account2.withdraw(2000, account1);
        LocalDateTime timeEnd = LocalDateTime.now().plusNanos(100);
        Entry expected = account1.history(timeStart, timeEnd)
                .stream()
                .max((o1, o2) -> (int) (o2.getAmount() - o1.getAmount()))
                .get();
        Entry actual = analyticsManager.maxExpenseAmountEntryWithinInterval(Arrays.asList(account1, account2), timeStart, timeEnd).get();
        assertEquals(expected, actual);
    }

}
