package parking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WalletTest {

    private Wallet w;

    @BeforeEach
    void setUp() {
        w = new Wallet(100.0);
    }

    @Test
    void testDefaultConstructor() {
        Wallet wallet = new Wallet();
        assertEquals(0.0, wallet.getBalance());
    }

    @Test
    void testParameterizedConstructor() {
        Wallet wallet = new Wallet(150.0);
        assertEquals(150.0, wallet.getBalance());
    }

    @Test
    void testZeroBalanceConstructor() {
        Wallet wallet = new Wallet(0.0);
        assertEquals(0.0, wallet.getBalance());
    }

    @Test
    void testNegativeBalance() {
        Wallet wallet = new Wallet(-100.0);
        assertEquals(-100.0, wallet.getBalance());
    }

    @Test
    void testSmallNegativeBalance() {
        Wallet wallet = new Wallet(-0.01);
        assertEquals(-0.01, wallet.getBalance(), 0.0001);
    }

    @Test
    void testLargeBalanceConstructor() {
        Wallet wallet = new Wallet(1_000_000.0);
        assertEquals(1_000_000.0, wallet.getBalance());
    }

    @Test
    void testBalanceCheck() {
        assertEquals(100.0, w.getBalance());
    }

    @Test
    void testAddFunds() {
        w.addFunds(50.0);
        assertEquals(150.0, w.getBalance());
    }

    @Test
    void testAddZeroFunds() {
        assertThrows(InvalidAmountException.class, () -> w.addFunds(0.0));
        assertEquals(100.0, w.getBalance());
    }

    @Test
    void testAddNegativeFunds() {
        assertThrows(InvalidAmountException.class, () -> w.addFunds(-20.0));
        assertEquals(100.0, w.getBalance());
    }

    @Test
    void testAddSmallNegativeFunds() {
        assertThrows(InvalidAmountException.class, () -> w.addFunds(-0.0001));
        assertEquals(100.0, w.getBalance());
    }

    @Test
    void testAddMultipleFunds() {
        w.addFunds(10.0);
        w.addFunds(20.0);
        w.addFunds(30.0);
        assertEquals(160.0, w.getBalance());
    }

    @Test
    void testAddFundsToNegativeWallet() {
        Wallet negWallet = new Wallet(-50.0);
        negWallet.addFunds(80.0);
        assertEquals(30.0, negWallet.getBalance());
    }

    @Test
    void testDeductFunds() {
        w.deductFunds(40.0);
        assertEquals(60.0, w.getBalance());
    }

    @Test
    void testDeductExactBalance() {
        w.deductFunds(100.0);
        assertEquals(0.0, w.getBalance());
    }

    @Test
    void testDeductMoreThanBalance() {
        assertThrows(InsufficientFundsException.class, () -> w.deductFunds(150.0));
        assertEquals(100.0, w.getBalance());
    }

    @Test
    void testDeductSlightlyMoreThanBalance() {
        assertThrows(InsufficientFundsException.class, () -> w.deductFunds(100.001));
        assertEquals(100.0, w.getBalance());
    }

    @Test
    void testDeductZeroFunds() {
        assertThrows(InvalidAmountException.class, () -> w.deductFunds(0.0));
        assertEquals(100.0, w.getBalance());
    }

    @Test
    void testDeductNegativeFunds() {
        assertThrows(InvalidAmountException.class, () -> w.deductFunds(-20.0));
        assertEquals(100.0, w.getBalance());
    }

    @Test
    void testDeductFromNegativeWallet() {
        Wallet negWallet = new Wallet(-10.0);
        assertThrows(InsufficientFundsException.class, () -> negWallet.deductFunds(5.0));
    }

    @Test
    void testTransferFunds() {
        Wallet receiver = new Wallet(20.0);
        w.transferFunds(receiver, 30.0);
        assertEquals(70.0, w.getBalance());
        assertEquals(50.0, receiver.getBalance());
    }

    @Test
    void testTransferExactBalance() {
        Wallet receiver = new Wallet(0.0);
        w.transferFunds(receiver, 100.0);
        assertEquals(0.0, w.getBalance());
        assertEquals(100.0, receiver.getBalance());
    }

    @Test
    void testTransferMoreThanBalance() {
        Wallet receiver = new Wallet(10.0);
        assertThrows(InsufficientFundsException.class, () -> w.transferFunds(receiver, 150.0));
        assertEquals(100.0, w.getBalance());
        assertEquals(10.0, receiver.getBalance());
    }

    @Test
    void testTransferZeroFunds() {
        Wallet receiver = new Wallet(10.0);
        assertThrows(InvalidAmountException.class, () -> w.transferFunds(receiver, 0.0));
        assertEquals(100.0, w.getBalance());
    }

    @Test
    void testTransferNegativeFunds() {
        Wallet receiver = new Wallet(10.0);
        assertThrows(InvalidAmountException.class, () -> w.transferFunds(receiver, -15.0));
        assertEquals(100.0, w.getBalance());
    }

    @Test
    void testTransferToNull() {
        assertThrows(NullPointerException.class, () -> w.transferFunds(null, 40.0));
        assertEquals(60.0, w.getBalance());
    }
}
