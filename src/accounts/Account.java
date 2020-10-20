package accounts;

import java.time.LocalDateTime;
import java.util.Collection;

public interface Account {
    double balanceOn(LocalDateTime date);
    void addEntry(Entry entry);
    Collection<Entry> history(LocalDateTime from, LocalDateTime to);
}
