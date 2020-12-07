package account;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.*;
import java.util.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestAnalyticsManager {

    private LocalDateTime systemDataTimeForTesting = LocalDateTime.now();
    private int minutesToAdd = 0;

    @Test
    void mostFrequentBeneficiaryOfAccount_shouldReturnCorrectBeneficiary() {
        TransactionManager transactionManager = new TransactionManager();
        AnalyticsManager analyticsManager = new AnalyticsManager(transactionManager);
        Account account1 = new Account(1, transactionManager);
        Account account2 = new Account(2, transactionManager);
        Account account3 = new Account(3, transactionManager);

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
        List<Transaction> transactions = new ArrayList<Transaction>();
        TransactionManager transactionManager = mock(TransactionManager.class);
        when(transactionManager.findAllTransactionsByAccount(null)).thenReturn(transactions);
        AnalyticsManager analyticsManager = new AnalyticsManager(transactionManager);
        for (int amount : List.of(1_456, 1_000, 911, 870, 150)) {
            transactions.add(new Transaction(1, amount, null, null));
        }
        Collection<Transaction> actualTransactions = analyticsManager.topTenExpensivePurchases(null);
        assertIterableEquals(transactions, actualTransactions);
    }
}
