import java.time.LocalDateTime;
import java.util.Collection;

public class DebitCard implements Account {
    private final long id;
    private final TransactionManager transactionManager;
    private final Entries entries;
    private final double bonusPercentage;
    private final BonusAccount bonusAccount;

    public DebitCard(long id, TransactionManager transactionManager, double bonusPercentage) {
        if (bonusPercentage < 0 || bonusPercentage > 1) {
            throw new  IllegalArgumentException("bonus percentage must be between 0 and 1");
        }
        this.id = id;
        this.transactionManager = transactionManager;
        this.bonusPercentage = bonusPercentage;
        this.bonusAccount = new BonusAccount();
        this.entries = new Entries();
    }

    public double getBonusPercentage() {
        return bonusPercentage;
    }

    /**
     * Withdraws money from account. <b>Should use TransactionManager to manage transactions</b>
     *
     * @param amount amount of money to withdraw
     * @return true
     * if amount &gt 0 and (currentBalance - amount) &ge 0,
     * otherwise returns false
     */
    public boolean withdraw(double amount, DebitCard beneficiary) {
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
    @Override
    public double balanceOn(LocalDateTime date) {
        double balance = 0;
        for (Entry entry : entries.to(date)) {
            balance += entry.getAmount();
        }
        return balance + bonusAccount.balanceOn(date);
    }

    /**
     * Finds the last transaction of the account and rollbacks it
     */
    public void rollbackLastTransaction() {
        transactionManager.rollbackLastTransactionByAccount(this);
    }

    @Override
    public void addEntry(Entry entry) {
        entries.addEntry(entry);
    }

    void addEntryToBonusAccount(Entry entry) {
        bonusAccount.addEntry(entry);
    }

    private boolean canWithdraw(double amount) {
        return amount > 0 && (balanceOn(LocalDateTime.now()) - amount) > 0;
    }

}
