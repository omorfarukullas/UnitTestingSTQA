package parking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleTest {

    private Vehicle v;
    private Wallet w;

    @BeforeEach
    void setUp() {
        w = new Wallet(100.0);
        v = new Vehicle(1, VehicleType.CAR, w);
    }

    @Test
    void testConstructorWithWallet() {
        assertEquals(1, v.getVehicleId());
        assertEquals(VehicleType.CAR, v.getVehicleType());
        assertSame(w, v.getWallet());
    }

    @Test
    void testBalanceCheck() {
        assertEquals(100.0, v.getBalance());
    }

    @Test
    void testConstructorWithZeroBalance() {
        Vehicle vehicle = new Vehicle(3, VehicleType.BICYCLE, 0.0);
        assertEquals(0.0, vehicle.getBalance());
    }

    @Test
    void testConstructorWithDoubleBalance() {
        Vehicle vehicle = new Vehicle(4, VehicleType.BUS, 200.0);
        assertNotNull(vehicle.getWallet());
        assertEquals(200.0, vehicle.getBalance());
    }

    @Test
    void testNegativeBalance() {
        Vehicle vehicle = new Vehicle(5, VehicleType.CAR, -100.0);
        assertEquals(-100.0, vehicle.getBalance());
    }

    @Test
    void testNegativeWallet() {
        Wallet negWallet = new Wallet(-50.0);
        Vehicle vehicle = new Vehicle(6, VehicleType.MOTORCYCLE, negWallet);
        assertEquals(-50.0, vehicle.getBalance());
    }

    @Test
    void testGetVehicleId() {
        assertEquals(1, v.getVehicleId());
    }

    @Test
    void testGetVehicleType() {
        assertEquals(VehicleType.CAR, v.getVehicleType());
    }

    @Test
    void testBalanceReflectsWalletUpdates() {
        w.addFunds(30.0);
        assertEquals(130.0, v.getBalance());
    }

    @Test
    void testAllVehicleTypes() {
        for (VehicleType type : VehicleType.values()) {
            Vehicle vehicle = new Vehicle(999, type, 10.0);
            assertEquals(type, vehicle.getVehicleType());
        }
    }

    @Test
    void testToString() {
        String str = v.toString();
        assertNotNull(str);
        assertTrue(str.contains("1"));
        assertTrue(str.contains("CAR"));
        assertTrue(str.contains("100"));
    }

    @Test
    void testNullWalletThrows() {
        Vehicle vehicle = new Vehicle(99, VehicleType.CAR, (Wallet) null);
        assertNull(vehicle.getWallet());
        assertThrows(NullPointerException.class, vehicle::getBalance);
    }
}
