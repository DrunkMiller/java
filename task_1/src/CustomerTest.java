import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void createCustomer_throwsException_whenNameNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Customer customerWithEmptyLine = new Customer(null, "Petrov");
        });
    }

    @Test
    void createCustomer_throwsException_whenNameEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            Customer customerWithEmptyLine = new Customer("", "Petrov");
        });
    }

    @Test
    void createCustomer_throwsException_whenLastNameNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Customer customerWithEmptyLine = new Customer("Petr", null);
        });
    }

    @Test
    void createCustomer_throwsException_whenLastNameEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            Customer customerWithEmptyLine = new Customer("Petr", "");
        });
    }

    @Test
    void openAccount_returnTrue_whenAccountNotCreated() {
        Customer customer = new Customer("Petr", "Petrov");
        assertTrue(customer.openAccount(111));
    }

    @Test
    void openAccount_returnFalse_whenAccountAlreadyCreated() {
        Customer customer = new Customer("Petr", "Petrov");
        customer.openAccount(111);
        assertFalse(customer.openAccount(123));
    }

    @Test
    void closeAccount_returnTrue_whenAccountExists() {
        Customer customer = new Customer("Petr", "Petrov");
        customer.openAccount(111);
        assertTrue(customer.closeAccount());
    }

    @Test
    void closeAccount_returnFalse_whenAccountNotExists() {
        Customer customer = new Customer("Petr", "Petrov");
        assertFalse(customer.closeAccount());
    }

    @Test
    void fullName_shouldReturnCorrectFormatFullName() {
        Customer customer = new Customer("Petr", "Petrov");
        assertEquals("Petr Petrov", customer.fullName());
    }

    @Test
    void withdrawFromCurrentAccount_returnFalse_whenAccountNotAlreadyCreated() {
        Customer customer = new Customer("Petr", "Petrov");
        assertFalse(customer.withdrawFromCurrentAccount(100));
    }

    @Test
    void addMoneyToCurrentAccount_returnFalse_whenAccountNotAlreadyCreated() {
        Customer customer = new Customer("Petr", "Petrov");
        assertFalse(customer.addMoneyToCurrentAccount(100));
    }
}