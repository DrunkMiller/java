package task1;

import org.junit.Test;
import static org.junit.Assert.*;

class AccountTest {
    @Test
    void testWithdraw() {
        Account account = new Account(1234);
        assertFalse(account.withdraw(100));
        assertFalse(account.withdraw(-111));
        account.add(100);
        assertTrue(account.withdraw(50));
        assertTrue(account.withdraw(50));
    }

    @Test
    void testAdd() {
        Account account = new Account(1234);
        assertFalse(account.add(-100));
        assertTrue(account.add(50));
    }
}