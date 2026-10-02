package parking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingSystemTest {

    private ParkingSystem system;
    private LocalDateTime base;

    @BeforeEach
    void setUp() {
        system = ParkingSystem.getInstance();
        system.resetForTesting();
        base = LocalDateTime.of(2025, 6, 1, 10, 0);
    }

    @Test
    void testSingleton() {
        ParkingSystem a = ParkingSystem.getInstance();
        ParkingSystem b = ParkingSystem.getInstance();
        assertSame(a, b);
    }

    @Test
    void testResetForTesting() {
        system.addVehicle(new Vehicle(1, VehicleType.CAR, 100.0));
        system.addParkingSlot(new ParkingSlot("S1", ParkingSlotType.REGULAR));
        system.setPARKING_RATE_PER_HOUR(25.0);
        system.getSYSTEM_WALLET().addFunds(50.0);
        system.resetForTesting();
        assertTrue(system.getVehicles().isEmpty());
        assertTrue(system.getParkingSlots().isEmpty());
        assertTrue(system.getBookings().isEmpty());
        assertEquals(10.0, system.getPARKING_RATE_PER_HOUR(), 1e-6);
        assertEquals(0.0, system.getBalance(), 1e-6);
    }

    @Test
    void testVehiclesManagement() {
        Vehicle v1 = new Vehicle(1, VehicleType.CAR, 200.0);
        system.addVehicle(v1);
        assertEquals(1, system.getVehicles().size());
        List<Vehicle> newList = new ArrayList<>();
        Vehicle v2 = new Vehicle(2, VehicleType.BUS, 300.0);
        newList.add(v2);
        system.setVehicles(newList);
        assertSame(v2, system.getVehicles().get(0));
    }

    @Test
    void testParkingSlotsManagement() {
        ParkingSlot s1 = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        system.addParkingSlot(s1);
        assertEquals(1, system.getParkingSlots().size());
        List<ParkingSlot> newList = new ArrayList<>();
        ParkingSlot s2 = new ParkingSlot("S2", ParkingSlotType.LARGE);
        newList.add(s2);
        system.setParkingSlots(newList);
        assertSame(s2, system.getParkingSlots().get(0));
    }

    @Test
    void testSetBookings() {
        List<Booking> list = new ArrayList<>();
        Booking b = new Booking(1, new Vehicle(1, VehicleType.CAR, 100.0),
                new ParkingSlot("S1", ParkingSlotType.REGULAR), base, base.plusHours(1), 10.0);
        list.add(b);
        system.setBookings(list);
        assertEquals(1, system.getBookings().size());
        assertSame(b, system.getBookings().get(0));
    }

    @Test
    void testSetSystemWallet() {
        Wallet customWallet = new Wallet(500.0);
        system.setSYSTEM_WALLET(customWallet);
        assertSame(customWallet, system.getSYSTEM_WALLET());
        assertEquals(500.0, system.getBalance(), 1e-6);
    }

    @Test
    void testGetAvailableSlotsFiltersCompatible() {
        ParkingSlot regular = new ParkingSlot("R1", ParkingSlotType.REGULAR);
        ParkingSlot compact = new ParkingSlot("C1", ParkingSlotType.COMPACT);
        system.addParkingSlot(regular);
        system.addParkingSlot(compact);
        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        List<ParkingSlot> available = system.getAvailableParkingSlots(car, base, base.plusHours(2));
        assertTrue(available.contains(regular));
        assertFalse(available.contains(compact));
    }

    @Test
    void testGetAvailableSlotsExcludesInactive() {
        ParkingSlot inactive = new ParkingSlot("R2", ParkingSlotType.REGULAR);
        inactive.deactivate();
        system.addParkingSlot(inactive);
        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        assertFalse(system.getAvailableParkingSlots(car, base, base.plusHours(2)).contains(inactive));
    }

    @Test
    void testGetAvailableSlotsExcludesOccupied() {
        ParkingSlot slot = new ParkingSlot("R3", ParkingSlotType.REGULAR);
        system.addParkingSlot(slot);
        system.book(new Vehicle(1, VehicleType.CAR, 500.0), slot, base, base.plusHours(2));
        Vehicle car2 = new Vehicle(2, VehicleType.CAR, 500.0);
        assertFalse(system.getAvailableParkingSlots(car2, base, base.plusHours(2)).contains(slot));
    }

    @Test
    void testBookEndBeforeStartThrows() {
        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        assertThrows(IllegalBookingTimeException.class,
                () -> system.book(car, slot, base.plusHours(2), base));
    }

    @Test
    void testBookEndEqualsStartThrows() {
        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        assertThrows(IllegalBookingTimeException.class,
                () -> system.book(car, slot, base, base));
    }

    @Test
    void testBookIncompatibleSlotThrows() {
        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot compact = new ParkingSlot("C1", ParkingSlotType.COMPACT);
        assertThrows(IllegalArgumentException.class,
                () -> system.book(car, compact, base, base.plusHours(2)));
    }

    @Test
    void testBookInactiveSlotThrows() {
        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        slot.deactivate();
        assertThrows(IllegalArgumentException.class,
                () -> system.book(car, slot, base, base.plusHours(2)));
    }

    @Test
    void testBookSuccess() {
        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        Booking b = system.book(car, slot, base, base.plusHours(2));
        assertEquals(20.0, b.getAmount(), 1e-6);
        assertEquals(480.0, car.getBalance(), 1e-6);
        assertEquals(20.0, system.getBalance(), 1e-6);
        assertTrue(system.getBookings().contains(b));
        assertTrue(slot.getBookings().contains(b));
        assertEquals(BookingStatus.ACTIVE, b.getBookingStatus());
    }

    @Test
    void testBookExactBalance() {
        Vehicle car = new Vehicle(1, VehicleType.CAR, 20.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        system.book(car, slot, base, base.plusHours(2));
        assertEquals(0.0, car.getBalance(), 1e-6);
        assertEquals(20.0, system.getBalance(), 1e-6);
    }

    @Test
    void testBookZeroBalanceThrows() {
        Vehicle car = new Vehicle(1, VehicleType.CAR, 0.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        assertThrows(InsufficientFundsException.class,
                () -> system.book(car, slot, base, base.plusHours(2)));
    }

    @Test
    void testBookInsufficientFundsThrows() {
        Vehicle car = new Vehicle(1, VehicleType.CAR, 15.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        assertThrows(InsufficientFundsException.class,
                () -> system.book(car, slot, base, base.plusHours(2)));
    }

    @Test
    void testBookOrphan() {
        Vehicle poorCar = new Vehicle(1, VehicleType.CAR, 0.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        assertThrows(InsufficientFundsException.class,
                () -> system.book(poorCar, slot, base, base.plusHours(2)));
        assertEquals(1, system.getBookings().size());
        assertTrue(slot.getBookings().isEmpty());
    }

    @Test
    void testPricingCarRegular() {
        Vehicle v = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot s = new ParkingSlot("S", ParkingSlotType.REGULAR);
        assertEquals(20.0, system.book(v, s, base, base.plusHours(2)).getAmount(), 1e-6);
    }

    @Test
    void testPricingMotorcycleCompact() {
        Vehicle v = new Vehicle(1, VehicleType.MOTORCYCLE, 500.0);
        ParkingSlot s = new ParkingSlot("S", ParkingSlotType.COMPACT);
        assertEquals(8.0, system.book(v, s, base, base.plusHours(2)).getAmount(), 1e-6);
    }

    @Test
    void testPricingBicycleRegular() {
        Vehicle v = new Vehicle(1, VehicleType.BICYCLE, 500.0);
        ParkingSlot s = new ParkingSlot("S", ParkingSlotType.REGULAR);
        assertEquals(2.0, system.book(v, s, base, base.plusHours(1)).getAmount(), 1e-6);
    }

    @Test
    void testPricingBusLarge() {
        Vehicle v = new Vehicle(1, VehicleType.BUS, 500.0);
        ParkingSlot s = new ParkingSlot("S", ParkingSlotType.LARGE);
        assertEquals(90.0, system.book(v, s, base, base.plusHours(3)).getAmount(), 1e-6);
    }

    @Test
    void testPricingCustomHourlyRate() {
        system.setPARKING_RATE_PER_HOUR(20.0);
        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot slot = new ParkingSlot("S", ParkingSlotType.REGULAR);
        assertEquals(40.0, system.book(car, slot, base, base.plusHours(2)).getAmount(), 1e-6);
    }

    @Test
    void testPricingTruncate() {
        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        Booking b = system.book(car, slot, base, base.plusMinutes(90));
        assertEquals(10.0, b.getAmount(), 1e-6);
    }

    @Test
    void testSubHourCrash() {
        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        assertThrows(InvalidAmountException.class,
                () -> system.book(car, slot, base, base.plusMinutes(30)));
        assertEquals(1, system.getBookings().size());
    }

    @Test
    void testCompleteBooking() {
        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        Booking b = system.book(car, slot, base, base.plusHours(2));
        system.completeBooking(b);
        assertEquals(BookingStatus.COMPLETED, b.getBookingStatus());
        assertEquals(16.0, slot.getBalance(), 1e-6);
        assertEquals(4.0, system.getBalance(), 1e-6);
    }

    @Test
    void testCompleteBookingCorruptedStatus() {
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        Booking zeroBooking = new Booking(1, car, slot, base, base.plusHours(1), 0.0);
        assertThrows(InvalidAmountException.class, () -> system.completeBooking(zeroBooking));
        assertEquals(BookingStatus.COMPLETED, zeroBooking.getBookingStatus());
    }

    @Test
    void testCancelBooking() {
        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        Booking b = system.book(car, slot, base, base.plusHours(2));
        system.cancelBooking(b);
        assertEquals(BookingStatus.CANCELLED, b.getBookingStatus());
        assertEquals(498.0, car.getBalance(), 1e-6);
        assertEquals(2.0, system.getBalance(), 1e-6);
    }

    @Test
    void testCancelBookingCorruptedStatus() {
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        Booking zeroBooking = new Booking(1, car, slot, base, base.plusHours(1), 0.0);
        assertThrows(InvalidAmountException.class, () -> system.cancelBooking(zeroBooking));
        assertEquals(BookingStatus.CANCELLED, zeroBooking.getBookingStatus());
    }

    @Test
    void testDoubleBookingSameSlotThrows() {
        Vehicle car1 = new Vehicle(1, VehicleType.CAR, 500.0);
        Vehicle car2 = new Vehicle(2, VehicleType.CAR, 500.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        system.book(car1, slot, base, base.plusHours(2));
        assertThrows(IllegalArgumentException.class,
                () -> system.book(car2, slot, base, base.plusHours(2)));
    }

    @Test
    void testAdjacentBookingsAllowed() {
        Vehicle car1 = new Vehicle(1, VehicleType.CAR, 500.0);
        Vehicle car2 = new Vehicle(2, VehicleType.CAR, 500.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        system.book(car1, slot, base, base.plusHours(2));
        assertDoesNotThrow(() -> system.book(car2, slot, base.plusHours(2), base.plusHours(4)));
    }

    @Test
    void testCancelledBookingBlocksRebooking() {
        Vehicle car1 = new Vehicle(1, VehicleType.CAR, 500.0);
        Vehicle car2 = new Vehicle(2, VehicleType.CAR, 500.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        Booking b1 = system.book(car1, slot, base, base.plusHours(2));
        system.cancelBooking(b1);
        assertThrows(IllegalArgumentException.class,
                () -> system.book(car2, slot, base, base.plusHours(2)));
    }
}
