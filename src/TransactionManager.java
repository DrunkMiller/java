import java.util.*;

public class TransactionManager {
    /**
     * Creates and stores transactions
     * @param amount
     * @param originator
     * @param beneficiary
     * @return created Transaction
     */

    private final LinkedList<Transaction> transactions;
    private int transactionsIdCounter;

    public TransactionManager() {
        this.transactions = new LinkedList<Transaction>();
        transactionsIdCounter = 0;
    }

    public Transaction createTransaction(double amount, DebitCard originator, DebitCard beneficiary) {
        Transaction transaction = new Transaction(transactionsIdCounter++, amount, originator, beneficiary);
        transactions.add(transaction);
        return transaction;
    }

    public Collection<Transaction> findAllTransactionsByAccount(DebitCard account) {
        List<Transaction> transactionsByAccount = new LinkedList<Transaction>();
        for (Transaction transaction : transactions) {
            if (isTransactionMember(account, transaction)) {
                transactionsByAccount.add(transaction);
            }
        }
        return transactionsByAccount;
    }

    public void rollbackTransaction(Transaction transaction) {
        transactions.add(transaction.rollback());
    }

    public void rollbackLastTransactionByAccount(DebitCard account) {
        LinkedList<Transaction> transactionsByAccount = new LinkedList<Transaction>(findAllTransactionsByAccount(account));
        rollbackTransaction(transactionsByAccount.getLast());
    }

    public void executeTransaction(Transaction transaction) {
        transactions.add(transaction.execute());
    }

    private boolean isTransactionMember(DebitCard account, Transaction transaction) {
        return (transaction.getBeneficiary() != null && transaction.getBeneficiary().equals(account))
                || (transaction.getOriginator() != null && transaction.getOriginator().equals(account));
    }
}
