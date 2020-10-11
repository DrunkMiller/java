package accounts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Collection of entries for the account. Use it to save and get history of payments
 */
public class Entries {
    private final LinkedList<Entry> entries;

    public Entries() {
        this.entries = new LinkedList<Entry>();
    }

    void addEntry(Entry entry) {
        entries.add(entry);
    }

    Collection<Entry> from(LocalDateTime date) {
        List<Entry> entriesFrom = new ArrayList<Entry>();
        for (Entry entry: entries) {
            if (date.isBefore(entry.getTime())) {
                entriesFrom.add(entry);
            }
        }
        return entriesFrom;
    }

    Collection<Entry> to(LocalDateTime date) {
        List<Entry> entriesFrom = new ArrayList<Entry>();
        for (Entry entry: entries) {
            if (date.isAfter(entry.getTime())) {
                entriesFrom.add(entry);
            }
        }
        return entriesFrom;
    }

    Collection<Entry> betweenDates(LocalDateTime from, LocalDateTime to) {
        List<Entry> entriesBetween = new ArrayList<Entry>();
        for (Entry entry: entries) {
            if (from.isBefore(entry.getTime()) && to.isAfter(entry.getTime())) {
                entriesBetween.add(entry);
            }
        }
        return entriesBetween;
    }

    Entry last() {
        return entries.getLast();
    }
}