package transactions;

import accounts.Account;
import accounts.DebitCard;
import accounts.Entry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Transaction {
    private final long id;
    private final double amount;
    private final Account originator;
    private final Account beneficiary;
    private final boolean executed;
    private final boolean rolledBack;
    private final Collection<Transaction> dependentTransactions;

    public Transaction(long id, double amount, Account originator, Account beneficiary) {
        this.id = id;
        this.amount = amount;
        this.originator = originator;
        this.beneficiary = beneficiary;
        this.executed = false;
        this.rolledBack = false;
        dependentTransactions = new ArrayList<>();
    }

    private Transaction(Transaction transaction, boolean executed, boolean rolledBack) {
        this.id = transaction.id;
        this.amount = transaction.amount;
        this.originator = transaction.originator;
        this.beneficiary = transaction.beneficiary;
        this.executed = executed;
        this.rolledBack = rolledBack;
        dependentTransactions = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public Account getOriginator() {
        return originator;
    }

    public Account getBeneficiary() {
        return beneficiary;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isRolledBack() {
        return rolledBack;
    }

    public boolean isExecuted() {
        return executed;
    }

    public Collection<Transaction> getDependentTransactions() {
        return dependentTransactions;
    }

    public void addDependentTransaction(Transaction transaction) {
        dependentTransactions.add(transaction);
    }

    /**
     * Adding entries to both main.java.accounts
     * @throws IllegalStateException when was already executed
     */
    public Transaction execute() {
        if (executed) {
            throw new IllegalStateException("transactions.Transaction was already executed");
        }
        Transaction executedTransaction = new Transaction(this, true, false);
        addEntiesToOriginator(executedTransaction, -amount);
        addEntriesToBeneficiary(executedTransaction, amount);
        return executedTransaction;
    }

    /**
     * Removes all entries of current transaction from originator and beneficiary
     * @throws IllegalStateException when was already rolled back
     */
    public Transaction rollback() {
        if (rolledBack || !executed) {
            throw new IllegalStateException("transactions.Transaction was already rolled back");
        }
        Transaction rolledBackTransaction = new Transaction(this, false, true);
        addEntiesToOriginator(rolledBackTransaction, amount);
        addEntriesToBeneficiary(rolledBackTransaction, -amount);
        return rolledBackTransaction;
    }

    private void addEntiesToOriginator(Transaction transaction, double amount) {
        if (transaction.originator != null) {
            Entry originatorEntry = new Entry(transaction.originator, transaction, amount, LocalDateTime.now());
            originatorEntry.accept();
        }
    }

    private void addEntriesToBeneficiary(Transaction transaction, double amount) {
        if (transaction.beneficiary != null) {
            Entry beneficiaryEntry = new Entry(transaction.beneficiary, transaction, amount, LocalDateTime.now());
            beneficiaryEntry.accept();
        }
    }
}
