package storage;

import transactions.Transaction;

public class TransactionKeyExtractor implements KeyExtractor<Long, Transaction> {
    @Override
    public Long extract(Transaction entity) {
        return entity.getId();
    }
}
