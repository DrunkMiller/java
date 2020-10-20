package accounts;

import java.time.LocalDateTime;
import java.util.Collection;

public class BonusAccount implements Account {
    private final Entries entries;
    private final double bonusPercentage;

    public BonusAccount(double bonusPercentage) {
        if (bonusPercentage < 0 || bonusPercentage > 1) {
            throw new  IllegalArgumentException("Bonus percentage must be between 0 and 1");
        }
        this.bonusPercentage = bonusPercentage;
        this.entries = new Entries();
    }

    public double getBonusPercentage() {
        return bonusPercentage;
    }

    @Override
    public double balanceOn(LocalDateTime date) {
        double balance = 0;
        for (Entry entry : entries.to(date)) {
            balance += entry.getAmount();
        }
        return balance;
    }

    @Override
    public void addEntry(Entry entry) {
        entries.addEntry(entry);
    }

    @Override
    public Collection<Entry> history(LocalDateTime from, LocalDateTime to) {
        return entries.betweenDates(from, to);
    }
}
