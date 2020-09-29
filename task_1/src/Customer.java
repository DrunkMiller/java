public class Customer {
    private final String name;
    private final String lastName;
    private Account account;

    public Customer(String name, String lastName) {
        if (!isValidName(name) || !isValidLastName(lastName))
            throw new IllegalArgumentException("Invalid string format");
        this.name = name;
        this.lastName = lastName;
    }

    /**
     * Opens account for a customer (creates Account and sets it to field "account").
     * Customer can't have greater than one opened account.
     *
     * @param accountId id of the account
     * @return true if account hasn't already created, otherwise returns false and prints "Customer fullName() already has the active account"
     */
    public boolean openAccount(long accountId) {
        if (account != null) {
            System.out.println(String.format("Customer %s already has the active account", fullName()));
            return false;
        }
        account = new Account(accountId);
        return true;
    }

    /**
     * Closes account. Sets account to null.
     *
     * @return false if account is already null and prints "Customer fullName() has no active account to close", otherwise sets account to null and returns true
     */
    public boolean closeAccount() {
        if (account == null) {
            System.out.println(String.format("Customer %s has no active account to close", fullName()));
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
        return String.format("%s %s", name, lastName);
    }

    /**
     * Delegates withdraw to Account class
     * @param amount
     * @return false if account is null and prints "Customer fullName() has no active account", otherwise returns the result of Account's withdraw method
     */
    public boolean withdrawFromCurrentAccount(double amount) {
        if (account == null) {
            System.out.println(String.format("Customer %s has no active account", fullName()));
            return false;
        }
        return account.withdraw(amount);
    }

    /**
     * Delegates adding money to Account class
     * @param amount
     * @return false if account is null and prints "Customer fullName() has no active account", otherwise returns the result of Account's add method
     */
    public boolean addMoneyToCurrentAccount(double amount) {
        if (account == null) {
            System.out.println(String.format("Customer %s has no active account", fullName()));
            return false;
        }
        return account.add(amount);
    }

    private boolean isValidName(String name) {
        return name != "" && name != null;
    }

    private boolean isValidLastName(String lastName) {
        return lastName != "" && lastName != null;
    }
}
