package transactions;

import accounts.DebitCard;
import accounts.Entry;

import java.time.LocalDateTime;

public class Transaction {
    private final long id;
    private final double amount;
    private final DebitCard originator;
    private final DebitCard beneficiary;
    private final boolean executed;
    private final boolean rolledBack;

    public Transaction(long id, double amount, DebitCard originator, DebitCard beneficiary) {
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

    public long getId() {
        return id;
    }

    public DebitCard getOriginator() {
        return originator;
    }

    public DebitCard getBeneficiary() {
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
            throw new IllegalStateException("transactions.Transaction was already executed");
        }
        Transaction executedTransaction = new Transaction(this, true, false);
        if (originator != null) {
            Entry originatorEntry = new Entry(originator, executedTransaction, -amount, LocalDateTime.now());
            originatorEntry.accept();

        }
        if (originator != null && originator.getBonusAccount() != null) {
            Entry bonusEntry = new Entry(
                    originator.getBonusAccount(),
                    executedTransaction,
                    amount * originator.getBonusAccount().getBonusPercentage(),
                    LocalDateTime.now());
            bonusEntry.accept();
        }
        if (beneficiary != null) {
            Entry beneficiaryEntry = new Entry(beneficiary, executedTransaction, amount, LocalDateTime.now());
            beneficiaryEntry.accept();
        }
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
        if (originator != null) {
            Entry originatorEntry = new Entry(originator, rolledBackTransaction, amount, LocalDateTime.now());
            originatorEntry.accept();
            Entry bonusEntry = new Entry(
                    originator.getBonusAccount(),
                    rolledBackTransaction,
                    -amount * originator.getBonusAccount().getBonusPercentage(),
                    LocalDateTime.now());
            bonusEntry.accept();
        }
        if (originator != null && originator.getBonusAccount() != null) {
            Entry bonusEntry = new Entry(
                    originator.getBonusAccount(),
                    rolledBackTransaction,
                    -amount * originator.getBonusAccount().getBonusPercentage(),
                    LocalDateTime.now());
            bonusEntry.accept();
        }
        if (beneficiary != null) {
            Entry beneficiaryEntry = new Entry(beneficiary, rolledBackTransaction, -amount, LocalDateTime.now());
            beneficiaryEntry.accept();
        }
        return rolledBackTransaction;
    }
}
