import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class TestAnalyticsManager {
    @Test
    void mostFrequentBeneficiaryOfAccount_shouldReturnCorrectBeneficiary() throws InterruptedException {
        TransactionManager transactionManager = new TransactionManager();
        AnalyticsManager analyticsManager = new AnalyticsManager(transactionManager);
        DebitCard account1 = new DebitCard(1, transactionManager, 1);
        DebitCard account2 = new DebitCard(2, transactionManager, 1);
        DebitCard account3 = new DebitCard(3, transactionManager, 1);
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
}
