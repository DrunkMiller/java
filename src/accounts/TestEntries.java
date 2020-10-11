package accounts;

import accounts.DebitCard;
import accounts.Entries;
import accounts.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transactions.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.mock;

public class TestEntries {
    private Entries entries;
    Entry entry1, entry2, entry3, entry4;
    @BeforeEach
    void setUpEntries() {
        entries = new Entries();
        entry1 = new Entry(mock(DebitCard.class), mock(Transaction.class), 100,
                LocalDateTime.of(2019,2, 21, 4, 8));
        entry2 = new Entry(mock(DebitCard.class), mock(Transaction.class), 100,
                LocalDateTime.of(2019,7, 22, 12, 33));
        entry3 = new Entry(mock(DebitCard.class), mock(Transaction.class), 100,
                LocalDateTime.of(2020,5, 11, 5, 42));
        entry4 = new Entry(mock(DebitCard.class), mock(Transaction.class), 100,
                LocalDateTime.of(2020,6, 12, 11, 9));
        entries.addEntry(entry1);
        entries.addEntry(entry2);
        entries.addEntry(entry3);
        entries.addEntry(entry4);
    }

    @Test
    void from_shouldReturnCorrectEntriesFromDate() {
        Collection<Entry> actualEntriesFromDate = entries.from(LocalDateTime.of(2020,1, 30, 11, 9));
        Collection<Entry> expectedEntriesFromDate = new ArrayList<Entry>();
        expectedEntriesFromDate.add(entry3);
        expectedEntriesFromDate.add(entry4);
        assertIterableEquals(expectedEntriesFromDate, actualEntriesFromDate);
    }

    @Test
    void to_shouldReturnCorrectEntriesToDate() {
        Collection<Entry> actualEntriesToDate = entries.to(LocalDateTime.of(2020,1, 30, 11, 9));
        Collection<Entry> expectedEntriesToDate = new ArrayList<Entry>();
        expectedEntriesToDate.add(entry1);
        expectedEntriesToDate.add(entry2);
        assertIterableEquals(expectedEntriesToDate, actualEntriesToDate);
    }

    @Test
    void to_shouldReturnCorrectEntriesBetweenDate() {
        Collection<Entry> actualEntriesBetweenDate = entries.betweenDates(
                LocalDateTime.of(2019,3, 10, 11, 9),
                LocalDateTime.of(2020,5, 15, 11, 9));
        Collection<Entry> expectedEntriesBetweenDate = new ArrayList<Entry>();
        expectedEntriesBetweenDate.add(entry2);
        expectedEntriesBetweenDate.add(entry3);
        assertIterableEquals(expectedEntriesBetweenDate, actualEntriesBetweenDate);
    }
}
