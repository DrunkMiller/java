package java;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TestTransaction {
    @Test
    void execute_throwsException_whenWasAlreadyExecuted() {
        Transaction transaction = new Transaction(1, 100, null, null);
        Transaction executedTransaction = transaction.execute();
        assertThrows(IllegalStateException.class, () -> {
            executedTransaction.execute();
        });
    }

    @Test
    void execute_returnExecutedTransaction_whenExecuted() {
        Transaction transaction = new Transaction(1, 100, null, null);
        Transaction executedTransaction = transaction.execute();
        assertTrue(executedTransaction.isExecuted());
    }

    @Test
    void execute_shouldCreateCorrectEntries() throws InterruptedException {
        TransactionManager transactionManager = new TransactionManager();
        Account account1 = new Account(1, transactionManager);
        Account account2 = new Account(2, transactionManager);
        account1.add(200);
        LocalDateTime currentDateTime = LocalDateTime.now();
        TimeUnit.MILLISECONDS.sleep(1);
        Transaction transaction = new Transaction(1, 100, account1, account2);
        transaction.execute();
        List<Entry> history1 = new ArrayList<Entry>(account1.history(currentDateTime, LocalDateTime.now().plusMinutes(1)));
        List<Entry> history2 = new ArrayList<Entry>(account2.history(currentDateTime, LocalDateTime.now().plusMinutes(1)));
        assertEquals(1, history1.size());
        assertEquals(1, history2.size());
        assertEquals(-100, history1.get(0).getAmount());
        assertEquals(100, history2.get(0).getAmount());
    }

    @Test
    void rollback_shouldCreateCorrectEntries() throws InterruptedException {
        TransactionManager transactionManager = new TransactionManager();
        Account account1 = new Account(1, transactionManager);
        Account account2 = new Account(2, transactionManager);
        account1.add(200);
        Transaction transaction = new Transaction(1, 100, account1, account2);
        Transaction executedTransaction = transaction.execute();
        LocalDateTime currentDateTime = LocalDateTime.now();
        TimeUnit.SECONDS.sleep(1);
        executedTransaction.rollback();
        List<Entry> history1 = new ArrayList<Entry>(account1.history(currentDateTime, LocalDateTime.now().plusMinutes(1)));
        List<Entry> history2 = new ArrayList<Entry>(account2.history(currentDateTime, LocalDateTime.now().plusMinutes(1)));
        assertEquals(1, history1.size());
        assertEquals(1, history2.size());
        assertEquals(100, history1.get(0).getAmount());
        assertEquals(-100, history2.get(0).getAmount());
    }

    @Test
    void rollback_throwsException_whenWasAlreadyRolledBack() {
        Transaction transaction = new Transaction(1, 100, null, null);
        Transaction executedTransaction = transaction.execute();
        Transaction rolledBackTransaction = executedTransaction.rollback();
        assertThrows(IllegalStateException.class, () -> {
            rolledBackTransaction.rollback();
        });
    }

    @Test
    void rollback_throwsException_whenWasNotAlreadyExecuted() {
        Transaction transaction = new Transaction(1, 100, null, null);
        assertThrows(IllegalStateException.class, () -> {
            transaction.rollback();
        });
    }

    @Test
    void rollback_returnRolledBackTransaction_whenRolledBack() {
        Transaction transaction = new Transaction(1, 100, null, null);
        Transaction executedTransaction = transaction.execute();
        Transaction rolledBackTransaction = executedTransaction.rollback();
        assertTrue(rolledBackTransaction.isRolledBack());
    }

}
