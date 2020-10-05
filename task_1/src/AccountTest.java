
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void withdrawMoney_returnsFalse_whenBalanceIsNotEnoughToWithdraw() {
        Account account = new Account(111);
        assertFalse(account.withdraw(100));
    }

    @Test
    void withdrawMoney_returnsFalse_whenAmountLessThanZero() {
        Account account = new Account(111);
        assertFalse(account.withdraw(-100));
    }

    @Test
    void withdrawMoney_returnsFalse_whenAmountAboveBalance() {
        Account account = new Account(111);
        account.add(100);
        assertFalse(account.withdraw(200));
    }

    @Test
    void withdrawMoney_returnsTrue_whenAmountSuit() {
        Account account = new Account(111);
        account.add(100);
        assertTrue(account.withdraw(50));
    }

    @Test
    void addMoney_returnsFalse_whenAmountLessZero() {
        Account account = new Account(111);
        assertFalse(account.add(-100));
    }

    @Test
    void addMoney_returnsTrue_whenAmountSuit() {
        Account account = new Account(111);
        assertTrue(account.add(50));
    }

}