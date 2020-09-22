package task1;

public class Customer {
    private final String name;
    private final String lastName;
    private Account account;

    public Customer(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    /**
     * Opens account for a customer (creates task1.Account and sets it to field "account").
     * task1.Customer can't have greater than one opened account.
     *
     * @param accountId id of the account
     * @return true if account hasn't already created, otherwise returns false and prints "task1.Customer fullName() already has the active account"
     */
    public boolean openAccount(long accountId) {
        if (account != null) {
            System.out.println(String.format(" task1.Customer %s %s already has the active account", fullName()));
            return false;
        }
        account = new Account(accountId);
        return true;
    }

    /**
     * Closes account. Sets account to null.
     *
     * @return false if account is already null and prints "task1.Customer fullName() has no active account to close", otherwise sets account to null and returns true
     */
    public boolean closeAccount() {
        if (account == null) {
            System.out.println(String.format(" task1.Customer %s %s has no active account to close", fullName()));
            return false;
        }
        account = null;
        return true;
    }

    /**
     * Formatted full name of the customer
     * @return concatenated form of name and lastName, e.g. "John Goodman"
     */
    public String fullName() {
//        StringBuilder fullName = new StringBuilder();
//        fullName.append(this.name != null ? this.name : "null");
//        fullName.append(" ");
//        fullName.append(this.lastName != null ? this.lastName : "null");
//        return fullName.toString();
        if (name == "")
            throw new IllegalArgumentException("Name can't be an empty string");
        if (lastName == "")
            throw new IllegalArgumentException("Last name can't be an empty string");
        return String.format("%s %s", this.name, this.lastName);
    }

    /**
     * Delegates withdraw to task1.Account class
     * @param amount
     * @return false if account is null and prints "task1.Customer fullName() has no active account", otherwise returns the result of task1.Account's withdraw method
     */
    public boolean withdrawFromCurrentAccount(double amount) {
        if (account == null) {
            System.out.println(String.format(" task1.Customer %s %s has no active account", fullName()));
            return false;
        }
        return account.withdraw(amount);
    }

    /**
     * Delegates adding money to task1.Account class
     * @param amount
     * @return false if account is null and prints "task1.Customer fullName() has no active account", otherwise returns the result of task1.Account's add method
     */
    public boolean addMoneyToCurrentAccount(double amount) {
        if (account == null) {
            System.out.println(String.format(" task1.Customer %s %s has no active account", fullName()));
            return false;
        }
        return account.add(amount);
    }
}
