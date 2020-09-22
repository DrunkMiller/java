package task1;

import org.junit.Test;

import static org.junit.Assert.*;

class CustomerTest {

    @Test
    void testOpenAccount() {
        Customer customer = new Customer("qwe", "asd");
        assertTrue(customer.openAccount(123));
        assertFalse(customer.openAccount(-123));
    }

    @Test
    void testCloseAccount() {
        Customer customer = new Customer("qwe", "asd");
        customer.openAccount(123);
        assertTrue(customer.closeAccount());
        assertFalse(customer.closeAccount());
    }

    @Test
    void testFullName() {
        Customer customerWithEmptyLine = new Customer("", "asd");
        assertThrows(IllegalArgumentException.class, () -> {
            customerWithEmptyLine.fullName();
        });
        Customer customerWithNullLine = new Customer(null, "asd");
        assertThrows(NullPointerException.class, () -> {
            customerWithEmptyLine.fullName();
        });
        Customer customer = new Customer("qwe", "asd");
        assertEquals(customer.fullName(), "qwe asd");
    }

    @Test
    void testWithdrawFromCurrentAccount() {
        Customer customer = new Customer("qwe", "asd");
        assertFalse(customer.withdrawFromCurrentAccount(123));
        customer.openAccount(12345);
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