package analytics;

import accounts.Account;
import accounts.DebitCard;
import storage.KeyExtractor;
import transactions.Transaction;
import transactions.TransactionManager;

import java.time.LocalDateTime;
import java.util.*;

public class AnalyticsManager {
    private final TransactionManager transactionManager;

    public AnalyticsManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public DebitCard mostFrequentBeneficiaryOfAccount(DebitCard account) {
        Map<DebitCard, Integer> transfersFrequency = new HashMap<>();
        for (var transaction : transactionManager.findAllTransactionsByAccount(account)) {
            DebitCard currentBeneficiary = transaction.getBeneficiary();
            transfersFrequency.putIfAbsent(currentBeneficiary, 0);
            transfersFrequency.put(currentBeneficiary, transfersFrequency.get(currentBeneficiary) + 1);
        }

        DebitCard mostFrequentBeneficiary = null;
        Integer maxFrequency = Integer.MIN_VALUE;
        for (Map.Entry<DebitCard, Integer> entry : transfersFrequency.entrySet()) {
            if (maxFrequency < entry.getValue()) {
                mostFrequentBeneficiary = entry.getKey();
                maxFrequency = entry.getValue();
            }
        }
        return mostFrequentBeneficiary;
    }

    public Collection<Transaction> topTenExpensivePurchases(DebitCard account) {
        List<Transaction> allTransactionsByAccount = new ArrayList<Transaction>(transactionManager.findAllTransactionsByAccount(account));
        allTransactionsByAccount.sort(Comparator.comparing(Transaction::getAmount));
        int numberTransactionsByAccount = allTransactionsByAccount.size();
        Collections.reverse(allTransactionsByAccount);
        return allTransactionsByAccount.subList(0, numberTransactionsByAccount > 10 ? 10 : numberTransactionsByAccount);
    }

    public double overallBalanceOfAccounts(List<? extends Account> accounts) {
        double overallBalance = 0;
        LocalDateTime dateTime = LocalDateTime.now();
        for (Account account : accounts) {
            overallBalance += account.balanceOn(dateTime);
        }
        return overallBalance;
    }

    public <T, K> Set<K> uniqueKeysOf(List<T> entities, KeyExtractor<K, T> extractor) {
        Set<K> uniqueKeys = new TreeSet<>();
        for (T entity : entities) {
            uniqueKeys.add(extractor.extract(entity));
        }
        return uniqueKeys;
    }

    public <T extends Account> List<T> accountsRangeFrom(List<T> accounts, T minAccount, Comparator<? super Account> comparator) {
        List<T> sortedAccounts = new ArrayList<>(accounts);
        sortedAccounts.sort(comparator);
        return sortedAccounts.subList(sortedAccounts.indexOf(minAccount), sortedAccounts.size());
    }

}
