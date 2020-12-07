package account;

import java.util.*;

public class AnalyticsManager {
    private final TransactionManager transactionManager;

    public AnalyticsManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Account mostFrequentBeneficiaryOfAccount(Account account) {
        Map<Account, Integer> transfersFrequency = new HashMap<>();
        for (var transaction : transactionManager.findAllTransactionsByAccount(account)) {
            Account currentBeneficiary = transaction.getBeneficiary();
            transfersFrequency.putIfAbsent(currentBeneficiary, 0);
            transfersFrequency.put(currentBeneficiary, transfersFrequency.get(currentBeneficiary) + 1);
        }

        Account mostFrequentBeneficiary = null;
        Integer maxFrequency = Integer.MIN_VALUE;
        for (Map.Entry<Account, Integer> entry : transfersFrequency.entrySet()) {
            if (maxFrequency < entry.getValue()) {
                mostFrequentBeneficiary = entry.getKey();
                maxFrequency = entry.getValue();
            }
        }
        return mostFrequentBeneficiary;
    }

    public Collection<Transaction> topTenExpensivePurchases(Account account) {
        List<Transaction> allTransactionsByAccount = new ArrayList<Transaction>(transactionManager.findAllTransactionsByAccount(account));
        allTransactionsByAccount.sort(Comparator.comparing(Transaction::getAmount));
        int numberTransactionsByAccount = allTransactionsByAccount.size();
        Collections.reverse(allTransactionsByAccount);
        return allTransactionsByAccount.subList(0, numberTransactionsByAccount > 10 ? 10 : numberTransactionsByAccount);
    }
}
