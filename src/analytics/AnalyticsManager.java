package analytics;

import accounts.Account;
import accounts.DebitCard;
import accounts.Entry;
import storage.KeyExtractor;
import storage.TransactionKeyExtractor;
import transactions.Transaction;
import transactions.TransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsManager {
    private final TransactionManager transactionManager;

    public AnalyticsManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public DebitCard mostFrequentBeneficiaryOfAccount(DebitCard account) {
        return transactionManager.findAllTransactionsByAccount(account)
                .stream()
                .filter(transaction -> transaction.getOriginator() != null && transaction.getOriginator().equals(account))
                .map(transaction -> transaction.getBeneficiary())
                .collect(Collectors.groupingBy(acc -> acc, Collectors.counting()))
                .entrySet()
                .stream()
                .max((o1, o2) -> (int) (o1.getValue() - o2.getValue()))
                .get().getKey();
    }

    public Collection<Transaction> topTenExpensivePurchases(DebitCard account) {
        return transactionManager.findAllTransactionsByAccount(account)
                .stream()
                .sorted((Transaction t1, Transaction t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
                .limit(10)
                .collect(Collectors.toList());
    }

    public double overallBalanceOfAccounts(List<? extends Account> accounts) {
        return accounts
                .stream()
                .map(account -> account.balanceOn(LocalDateTime.now()))
                .reduce((left, right) -> left + right)
                .get();
    }

    public <T, K extends Comparable<? super K>> Set<K> uniqueKeysOf(List<? extends T> entities, KeyExtractor<K, ? super T> extractor) {
        return entities
                .stream()
                .map(extractor::extract)
                .collect(Collectors.toSet());
    }

    public List<Account> accountsRangeFrom(List<? extends Account> accounts, Account minAccount, Comparator<Account> comparator) {
        return accounts
                .stream()
                .sorted(comparator)
                .filter(account -> comparator.compare(account, minAccount) >= 0)
                .collect(Collectors.toList());
    }

    Optional<Entry> maxExpenseAmountEntryWithinInterval(List<Account> accounts, LocalDateTime from, LocalDateTime to) {
        return accounts
                .stream()
                .map(account -> account.history(from, to))
                .flatMap(collection -> collection.stream())
                .filter(entry -> entry.getAmount() < 0)
                .max((o1, o2) -> (int) (o2.getAmount()- o1.getAmount()));
    }

}
