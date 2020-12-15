package accounts;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import transactions.TransactionManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestBonusAccount {

    private LocalDateTime systemDataTimeForTesting = LocalDateTime.now();
    private int minutesToAdd = 0;

    @Test
    void createBonusAccount_throwsException_whenBonusPercentNotBetween0and1() {
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> {
            new BonusAccount(-0.5);
        });
        assertEquals(thrown.getMessage(), "Bonus percentage must be between 0 and 1");
        thrown = assertThrows(IllegalArgumentException.class, () -> {
            new BonusAccount(1.5);
        });
        assertEquals(thrown.getMessage(), "Bonus percentage must be between 0 and 1");
    }

    @Test
    void balanceOn_shouldReturnCorrectBalanceOnDate() throws InterruptedException {
        double bonusPercentage = 0.1;
        TransactionManager transactionManager = new TransactionManager();
        DebitCard originatorAccount = new DebitCard(1, transactionManager, new BonusAccount(bonusPercentage));
        DebitCard beneficiaryAccount = new DebitCard(2, transactionManager, new BonusAccount(bonusPercentage));

        try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenAnswer(inv -> systemDataTimeForTesting.plusMinutes(++minutesToAdd));

            originatorAccount.addCash(1000);
            originatorAccount.withdraw(500, beneficiaryAccount);
            originatorAccount.withdraw(200, beneficiaryAccount);

            assertEquals(70, originatorAccount.getBonusAccount().balanceOn(LocalDateTime.now()));
            assertEquals(370, originatorAccount.balanceOn(LocalDateTime.now()));
        }

    }

}
