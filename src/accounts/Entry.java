package accounts;

import accounts.Account;
import transactions.Transaction;

import java.time.LocalDateTime;

public class Entry {
    private final Account account;
    private final Transaction transaction;
    private final double amount;
    private final LocalDateTime time;

    public Entry(Account account, Transaction transaction, double amount, LocalDateTime time) {
        this.account = account;
        this.transaction = transaction;
        this.amount = amount;
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public double getAmount() {
        return amount;
    }

    public void accept() {
        account.addEntry(this);
    }
}



