import java.time.LocalDateTime;

public class BonusAccount implements Account {
    private final Entries entries;

    public BonusAccount() {
        this.entries = new Entries();
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
}
