package account;

import java.time.LocalDateTime;

public class Transaction {
    private final long id;
    private final double amount;
    private final Account originator;
    private final Account beneficiary;
    private final boolean executed;
    private final boolean rolledBack;

    public Transaction(long id, double amount, Account originator, Account beneficiary) {
        this.id = id;
        this.amount = amount;
        this.originator = originator;
        this.beneficiary = beneficiary;
        this.executed = false;
        this.rolledBack = false;
    }

    private Transaction(Transaction transaction, boolean executed, boolean rolledBack) {
        this.id = transaction.id;
        this.amount = transaction.amount;
        this.originator = transaction.originator;
        this.beneficiary = transaction.beneficiary;
        this.executed = executed;
        this.rolledBack = rolledBack;
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

    /**
     * Adding entries to both accounts
     * @throws IllegalStateException when was already executed
     */
    public Transaction execute() {
        if (executed) {
            throw new IllegalStateException("Transaction was already executed");
        }
        Transaction executedTransaction = new Transaction(this, true, false);
        if (originator != null) {
            Entry originatorEntry = new Entry(originator, executedTransaction, -amount, LocalDateTime.now());
            originator.addEntry(originatorEntry);
        }
        if (beneficiary != null) {
            Entry beneficiaryEntry = new Entry(beneficiary, executedTransaction, amount, LocalDateTime.now());
            beneficiary.addEntry(beneficiaryEntry);
        }
        return executedTransaction;
    }

    /**
     * Removes all entries of current transaction from originator and beneficiary
     * @throws IllegalStateException when was already rolled back
     */
    public Transaction rollback() {
        if (rolledBack || !executed) {
            throw new IllegalStateException("Transaction was already rolled back");
        }
        Transaction rolledBackTransaction = new Transaction(this, false, true);
        if (originator != null) {
            Entry originatorEntry = new Entry(originator, rolledBackTransaction, amount, LocalDateTime.now());
            originator.addEntry(originatorEntry);
        }
        if (beneficiary != null) {
            Entry beneficiaryEntry = new Entry(beneficiary, rolledBackTransaction, -amount, LocalDateTime.now());
            beneficiary.addEntry(beneficiaryEntry);
        }
        return rolledBackTransaction;
    }
}
