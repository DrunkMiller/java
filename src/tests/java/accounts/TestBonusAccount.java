package accounts;

import org.junit.jupiter.api.Test;
import transactions.TransactionManager;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestBonusAccount {

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
        originatorAccount.addCash(1000);
        TimeUnit.MILLISECONDS.sleep(1);
        originatorAccount.withdraw(500, beneficiaryAccount);
        originatorAccount.withdraw(200, beneficiaryAccount);
        LocalDateTime currentDateTime = LocalDateTime.now().plusNanos(10);
        assertEquals(70, originatorAccount.getBonusAccount().balanceOn(currentDateTime));
        assertEquals(370, originatorAccount.balanceOn(currentDateTime));
    }

}
