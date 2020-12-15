package storage;

import org.junit.jupiter.api.Test;
import transactions.Transaction;

import java.util.Arrays;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

public class TestSimpleEntitiesStorage {
    @Test
    void save_shouldCorrectSave_WhenDiffEntities() {
        BankEntitiesStorage<Long, Transaction> storage = new SimpleEntitiesStorage<>(
                new TransactionKeyExtractor()
        );
        Transaction transaction1 = new Transaction(1, 100, null, null);
        Transaction transaction2 = new Transaction(2, 200, null, null);
        Transaction transaction3 = new Transaction(3, 300, null, null);
        storage.save(transaction1);
        storage.save(transaction2);
        storage.save(transaction3);
        Collection<Transaction> expected = Arrays.asList(transaction1, transaction2, transaction3);
        Collection<Transaction> actual = storage.findAll();
        assertIterableEquals(expected, actual);
    }

    @Test
    void save_shouldCorrectSave_WhenIdentEntities() {
        BankEntitiesStorage<Long, Transaction> storage = new SimpleEntitiesStorage<>(
                new TransactionKeyExtractor()
        );
        Transaction transaction1 = new Transaction(1, 100, null, null);
        storage.save(transaction1);
        storage.save(transaction1);
        storage.save(transaction1);
        Collection<Transaction> expected = Arrays.asList(transaction1);
        Collection<Transaction> actual = storage.findAll();
        assertIterableEquals(expected, actual);
    }

    @Test
    void saveAll_shouldCorrectSave_WhenDiffEntities() {
        BankEntitiesStorage<Long, Transaction> storage = new SimpleEntitiesStorage<>(
                new TransactionKeyExtractor()
        );
        Transaction transaction1 = new Transaction(1, 100, null, null);
        Transaction transaction2 = new Transaction(2, 200, null, null);
        Collection<Transaction> expected = Arrays.asList(transaction1, transaction2);
        storage.saveAll(Arrays.asList(transaction1, transaction1, transaction2));
        Collection<Transaction> actual = storage.findAll();
        assertIterableEquals(expected, actual);
    }

    @Test
    void saveAll_shouldCorrectSave_WhenIdentEntities() {
        BankEntitiesStorage<Long, Transaction> storage = new SimpleEntitiesStorage<>(
                new TransactionKeyExtractor()
        );
        Transaction transaction1 = new Transaction(1, 100, null, null);
        Transaction transaction2 = new Transaction(2, 200, null, null);
        Transaction transaction3 = new Transaction(3, 300, null, null);
        Collection<Transaction> expected = Arrays.asList(transaction1, transaction2, transaction3);
        storage.saveAll(expected);
        Collection<Transaction> actual = storage.findAll();
        assertIterableEquals(expected, actual);
    }

    @Test
    void findAll_shouldCorrectReturnSavedEntities() {
        BankEntitiesStorage<Long, Transaction> storage = new SimpleEntitiesStorage<>(
                new TransactionKeyExtractor()
        );
        Transaction transaction1 = new Transaction(1, 100, null, null);
        Transaction transaction2 = new Transaction(2, 200, null, null);
        Transaction transaction3 = new Transaction(3, 300, null, null);
        Collection<Transaction> expected = Arrays.asList(transaction1, transaction2, transaction3);
        storage.saveAll(expected);
        Collection<Transaction> actual = storage.findAll();
        assertIterableEquals(expected, actual);
    }

    @Test
    void findByKey_shouldCorrectReturnEntityByKey() {
        TransactionKeyExtractor extractor = new TransactionKeyExtractor();
        BankEntitiesStorage<Long, Transaction> storage = new SimpleEntitiesStorage<>(extractor);
        Transaction transaction1 = new Transaction(1, 100, null, null);
        Transaction transaction2 = new Transaction(2, 200, null, null);
        Transaction transaction3 = new Transaction(3, 300, null, null);
        storage.saveAll(Arrays.asList(transaction1, transaction2, transaction3));
        assertEquals(transaction1, storage.findByKey(extractor.extract(transaction1)));
        assertEquals(transaction2, storage.findByKey(extractor.extract(transaction2)));
        assertEquals(transaction3, storage.findByKey(extractor.extract(transaction3)));
    }

    @Test
    void deleteByKey_shouldCorrectDeleteEntityByKey() {
        TransactionKeyExtractor extractor = new TransactionKeyExtractor();
        BankEntitiesStorage<Long, Transaction> storage = new SimpleEntitiesStorage<>(extractor);
        Transaction transaction1 = new Transaction(1, 100, null, null);
        Transaction transaction2 = new Transaction(2, 200, null, null);
        Transaction transaction3 = new Transaction(3, 300, null, null);
        storage.saveAll(Arrays.asList(transaction1, transaction2, transaction3));
        storage.deleteByKey(extractor.extract(transaction2));
        Collection<Transaction> expected = Arrays.asList(transaction1, transaction3);
        Collection<Transaction> actual = storage.findAll();
        assertIterableEquals(expected, actual);
    }

    @Test
    void deleteAll_shouldCorrectDeleteEntities() {
        TransactionKeyExtractor extractor = new TransactionKeyExtractor();
        BankEntitiesStorage<Long, Transaction> storage = new SimpleEntitiesStorage<>(extractor);
        Transaction transaction1 = new Transaction(1, 100, null, null);
        Transaction transaction2 = new Transaction(2, 200, null, null);
        Transaction transaction3 = new Transaction(3, 300, null, null);
        storage.saveAll(Arrays.asList(transaction1, transaction2, transaction3));
        storage.deleteAll(Arrays.asList(transaction2, transaction3));
        Collection<Transaction> expected = Arrays.asList(transaction1);
        Collection<Transaction> actual = storage.findAll();
        assertIterableEquals(expected, actual);
    }
}
