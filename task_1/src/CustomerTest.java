import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    @Test
    void testCreateCustomer() {
        assertThrows(IllegalArgumentException.class, () -> {
            Customer customerWithEmptyLine = new Customer("", "asd");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Customer customerWithEmptyLine = new Customer(null, "asd");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Customer customerWithEmptyLine = new Customer("qwe", "");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Customer customerWithEmptyLine = new Customer("qwe", null);
        });
    }
    @Test
    void testOpenAccount() {
        Customer customer = new Customer("qwe", "asd");
        assertTrue(customer.openAccount(123));
        assertFalse(customer.openAccount(123));
    }

    @Test
    void testCloseAccount() {
        Customer customer = new Customer("qwe", "asd");
        customer.openAccount(1234);
        assertTrue(customer.closeAccount());
        assertFalse(customer.closeAccount());
    }

    @Test
    void testFullName() {
        Customer customer = new Customer("qwe", "asd");
        assertEquals(customer.fullName(), "qwe asd");
    }

    @Test
    void testWithdrawFromCurrentAccount() {
        Customer customer = new Customer("qwe", "asd");
        assertFalse(customer.withdrawFromCurrentAccount(123));
        customer.openAccount(1234);
        customer.addMoneyToCurrentAccount(100);
        assertFalse(customer.withdrawFromCurrentAccount(1234));
        assertFalse(customer.withdrawFromCurrentAccount(-50));
        assertTrue(customer.withdrawFromCurrentAccount(50));
        assertTrue(customer.withdrawFromCurrentAccount(50));
    }

    @Test
    void addMoneyToCurrentAccount() {
        Customer customer = new Customer("qwe", "asd");
        assertFalse(customer.addMoneyToCurrentAccount(123));
        customer.openAccount(12345);
        assertFalse(customer.addMoneyToCurrentAccount(-50));
        assertTrue(customer.addMoneyToCurrentAccount(50));
    }
}