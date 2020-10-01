import java.util.*;

public class AnalyticsManager {
    private final TransactionManager transactionManager;

    public AnalyticsManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Account mostFrequentBeneficiaryOfAccount(Account account) {
        Map<Account, Integer> transfersFrequency = new HashMap<Account, Integer>();
        for (Transaction transaction : transactionManager.findAllTransactionsByAccount(account)) {
            if (transaction.getOriginator() != null && transaction.getOriginator().equals(account)) {
                Account currentBeneficiary = transaction.getBeneficiary();
                if (transfersFrequency.containsKey(currentBeneficiary)) {
                    transfersFrequency.put(currentBeneficiary, transfersFrequency.get(currentBeneficiary) + 1);
                }
                else {
                    transfersFrequency.put(currentBeneficiary, 1);
                }
            }
        }
        Account mostFrequentBeneficiary = null;
        Integer maxFrequency = -1;
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
}
