import java.time.LocalDateTime;
import java.util.*;

public class AnalyticsManager {
    private final TransactionManager transactionManager;

    public AnalyticsManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public DebitCard mostFrequentBeneficiaryOfAccount(DebitCard account) {
        Map<DebitCard, Integer> transfersFrequency = new HashMap<DebitCard, Integer>();
        for (Transaction transaction : transactionManager.findAllTransactionsByAccount(account)) {
            if (transaction.getOriginator() != null && transaction.getOriginator().equals(account)) {
                DebitCard currentBeneficiary = transaction.getBeneficiary();
                if (transfersFrequency.containsKey(currentBeneficiary)) {
                    transfersFrequency.put(currentBeneficiary, transfersFrequency.get(currentBeneficiary) + 1);
                }
                else {
                    transfersFrequency.put(currentBeneficiary, 1);
                }
            }
        }
        DebitCard mostFrequentBeneficiary = null;
        Integer maxFrequency = -1;
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
        allTransactionsByAccount.sort(new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return Double.compare(t2.getAmount(), t1.getAmount());
            }
        });
        if (allTransactionsByAccount.size() <= 10) {
            return allTransactionsByAccount;
        }
        else {
            return allTransactionsByAccount.subList(0, 10);
        }
    }

    public double overallBalanceOfAccounts(List<? extends Account> accounts) {
        double overallBalance = 0;
        for (Account account : accounts) {
            overallBalance += account.balanceOn(LocalDateTime.now());
        }
        return overallBalance;
    }

    public <K extends Comparable<? super K>> Set<K> uniqueKeysOf(List<? extends Account> accounts, KeyExtractor<K, Account> extractor) {
        Set<K> uniqueKeys = new TreeSet<>();
        for (Account account : accounts) {
            uniqueKeys.add(extractor.extract(account));
        }
        return uniqueKeys;
    }

    public List<? extends Account> accountsRangeFrom(List<? extends Account> accounts, Account minAccount, Comparator<Account> comparator) {
        List<Account> sortedAccounts = new ArrayList<>(accounts);
        sortedAccounts.sort(comparator);
        return sortedAccounts.subList(sortedAccounts.indexOf(minAccount), sortedAccounts.size());
    }
}
