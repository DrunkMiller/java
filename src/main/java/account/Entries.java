package account;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Collection of entries for the account. Use it to save and get history of payments
 */
public class Entries {
    private final TreeMap<LocalDateTime, List<Entry>> entries;

    public Entries() {
        this.entries = new TreeMap<>();
    }

    void addEntry(Entry entry) {
        LocalDateTime entyDateTime = entry.getTime();
        if (!entries.containsKey(entyDateTime)) {
            entries.put(entyDateTime, new ArrayList<>());
        }
        entries.get(entyDateTime).add(entry);
    }

    Collection<Entry> from(LocalDateTime date) {
        List<Entry> entriesFrom = new ArrayList<Entry>();
        for (LocalDateTime dateTime : entries.keySet()) {
            if (date.isBefore(dateTime)) {
                entriesFrom.addAll(entries.get(dateTime));
            }
        }
        return entriesFrom;
    }

    Collection<Entry> to(LocalDateTime date) {
        List<Entry> entriesTo = new ArrayList<Entry>();
        for (LocalDateTime dateTime : entries.keySet()) {
            if (date.isAfter(dateTime)) {
                entriesTo.addAll(entries.get(dateTime));
            }
        }
        return entriesTo;
    }

    Collection<Entry> betweenDates(LocalDateTime from, LocalDateTime to) {
        List<Entry> entriesBetween = new ArrayList<Entry>();
        for (LocalDateTime dateTime : entries.keySet()) {
            if (from.isBefore(dateTime) && to.isAfter(dateTime)) {
                entriesBetween.addAll(entries.get(dateTime));
            }
        }
        return entriesBetween;
    }

    Entry last() {
        List<Entry> entriesFromLastDate = entries.get(entries.lastKey());
        return entriesFromLastDate.get(entriesFromLastDate.size() - 1);
    }
}