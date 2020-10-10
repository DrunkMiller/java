import java.time.LocalDateTime;

public interface Account {
    double balanceOn(LocalDateTime date);
    void addEntry(Entry entry);
}
