package account;

import java.time.LocalDateTime;
import java.util.Collection;

public class Account {
    private final long id;
    private final TransactionManager transactionManager;
    private final Entries entries;

    public Account(long id, TransactionManager transactionManager) {
        this.id = id;
        this.transactionManager = transactionManager;
        this.entries = new Entries();
    }

    /**
     * Withdraws money from account. <b>Should use TransactionManager to manage transactions</b>
     *
     * @param amount amount of money to withdraw
     * @return true
     * if amount &gt 0 and (currentBalance - amount) &ge 0,
     * otherwise returns false
     */
    public boolean withdraw(double amount, Account beneficiary) {
        if (!canWithdraw(amount)) {
            return false;
        }
        Transaction transaction = transactionManager.createTransaction(amount, this, beneficiary);
        transactionManager.executeTransaction(transaction);
        return true;
    }

    /**
     * Withdraws cash money from account. <b>Should use TransactionManager to manage transactions</b>
     *
     * @param amount amount of money to withdraw
     * @return true
     * if amount &gt 0 and (currentBalance - amount) &ge 0,
     * otherwise returns false
     */
    public boolean withdrawCash(double amount) {
        if (!canWithdraw(amount)) {
            return false;
        }
        Transaction transaction = transactionManager.createTransaction(amount, this, null);
        transactionManager.executeTransaction(transaction);
        return true;
    }

    /**
     * Adds cash money to account. <b>Should use TransactionManager to manage transactions</b>
     *
     * @param amount amount of money to add
     * @return true
     * if amount &gt 0,
     * otherwise returns false
     */
    public boolean addCash(double amount) {
        if (amount < 0) {
            return false;
        }
        Transaction transaction = transactionManager.createTransaction(amount, null, this);
        transactionManager.executeTransaction(transaction);
        return true;
    }

    /**
     * Adds money to account. <b>Should use TransactionManager to manage transactions</b>
     *
     * @param amount amount of money to add
     * @return true
     * if amount &gt 0,
     * otherwise returns false
     */
    public boolean add(double amount) {
        if (amount < 0) {
            return false;
        }
        Transaction transaction = transactionManager.createTransaction(amount, null, this);
        transactionManager.executeTransaction(transaction);
        return true;
    }


    public Collection<Entry> history(LocalDateTime from, LocalDateTime to) {
        return entries.betweenDates(from, to);
    }

    /**
     * Calculates balance on the accounting entries basis
     * @param date
     * @return balance
     */
    public double balanceOn(LocalDateTime date) {
        double balance = 0;
        for (Entry entry : entries.to(date)) {
            balance += entry.getAmount();
        }
        return balance;
    }

    /**
     * Finds the last transaction of the account and rollbacks it
     */
    public void rollbackLastTransaction() {
        transactionManager.rollbackLastTransactionByAccount(this);
    }

    void addEntry(Entry entry) {
        entries.addEntry(entry);
    }

    private boolean canWithdraw(double amount) {
        return amount > 0 && (balanceOn(LocalDateTime.now()) - amount) > 0;
    }
}
