package transactions;

import accounts.Account;
import accounts.DebitCard;

import java.util.*;

public class TransactionManager {
    /**
     * Creates and stores transactions
     * @param amount
     * @param originator
     * @param beneficiary
     * @return created transactions.Transaction
     */

    private final List<Transaction> transactions;
    private int transactionsIdCounter;

    public TransactionManager() {
        this.transactions = new ArrayList<>();
        transactionsIdCounter = 0;
    }

    public Transaction createTransaction(double amount, Account originator, Account beneficiary) {
        Transaction transaction = new Transaction(transactionsIdCounter++, amount, originator, beneficiary);
        transactions.add(transaction);
        return transaction;
    }

    public Collection<Transaction> findAllTransactionsByAccount(Account account) {
        List<Transaction> transactionsByAccount = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (isTransactionMember(account, transaction)) {
                transactionsByAccount.add(transaction);
            }
        }
        return transactionsByAccount;
    }

    public void rollbackTransaction(Transaction transaction) {
        transactions.add(transaction.rollback());
        for (Transaction dependent : transaction.getDependentTransactions()) {
            if (!transaction.isRolledBack()) rollbackTransaction(dependent);
        }
    }

    public void rollbackLastTransactionByAccount(Account account) {
        LinkedList<Transaction> transactionsByAccount = new LinkedList<Transaction>(findAllTransactionsByAccount(account));
        rollbackTransaction(transactionsByAccount.getLast());
    }

    public void executeTransaction(Transaction transaction) {
        transactions.add(transaction.execute());
        for (Transaction dependent : transaction.getDependentTransactions()) {
            if (!transaction.isExecuted()) executeTransaction(dependent);
        }
    }

    private boolean isTransactionMember(Account account, Transaction transaction) {
        return (transaction.getBeneficiary() != null && transaction.getBeneficiary().equals(account))
                || (transaction.getOriginator() != null && transaction.getOriginator().equals(account));
    }
}
