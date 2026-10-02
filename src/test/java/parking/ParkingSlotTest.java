package parking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingSlotTest {

    private ParkingSlot slot;
    private LocalDateTime base;

    @BeforeEach
    void setUp() {
        slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        base = LocalDateTime.of(2025, 1, 1, 10, 0);
    }

    @Test
    void testNewSlotActive() {
        assertTrue(slot.isActive());
    }

    @Test
    void testNewSlotBalance() {
        assertEquals(0.0, slot.getBalance(), 1e-6);
    }

    @Test
    void testNewSlotBookingsEmpty() {
        assertNotNull(slot.getBookings());
        assertTrue(slot.getBookings().isEmpty());
    }

    @Test
    void testActivateDeactivate() {
        slot.deactivate();
        assertFalse(slot.isActive());
        slot.activate();
        assertTrue(slot.isActive());
    }

    @Test
    void testInactiveSlotNotCompatible() {
        slot.deactivate();
        assertFalse(slot.isCompatible(VehicleType.CAR, base, base.plusHours(2)));
        assertFalse(slot.isCompatible(VehicleType.MOTORCYCLE, base, base.plusHours(1)));
        assertFalse(slot.isCompatible(VehicleType.BICYCLE, base, base.plusHours(1)));
    }

    @Test
    void testMotorcycleCompatibility() {
        assertTrue(new ParkingSlot("M1", ParkingSlotType.COMPACT).isCompatible(VehicleType.MOTORCYCLE, base, base.plusHours(1)));
        assertTrue(slot.isCompatible(VehicleType.MOTORCYCLE, base, base.plusHours(1)));
        assertTrue(new ParkingSlot("M3", ParkingSlotType.LARGE).isCompatible(VehicleType.MOTORCYCLE, base, base.plusHours(1)));
        assertFalse(new ParkingSlot("M4", ParkingSlotType.HANDICAPPED).isCompatible(VehicleType.MOTORCYCLE, base, base.plusHours(1)));
    }

    @Test
    void testCarCompatibility() {
        assertFalse(new ParkingSlot("C1", ParkingSlotType.COMPACT).isCompatible(VehicleType.CAR, base, base.plusHours(1)));
        assertTrue(slot.isCompatible(VehicleType.CAR, base, base.plusHours(1)));
        assertTrue(new ParkingSlot("C3", ParkingSlotType.LARGE).isCompatible(VehicleType.CAR, base, base.plusHours(1)));
        assertFalse(new ParkingSlot("C4", ParkingSlotType.HANDICAPPED).isCompatible(VehicleType.CAR, base, base.plusHours(1)));
    }

    @Test
    void testBusCompatibility() {
        assertFalse(new ParkingSlot("B1", ParkingSlotType.COMPACT).isCompatible(VehicleType.BUS, base, base.plusHours(1)));
        assertFalse(slot.isCompatible(VehicleType.BUS, base, base.plusHours(1)));
        assertTrue(new ParkingSlot("B3", ParkingSlotType.LARGE).isCompatible(VehicleType.BUS, base, base.plusHours(1)));
        assertFalse(new ParkingSlot("B4", ParkingSlotType.HANDICAPPED).isCompatible(VehicleType.BUS, base, base.plusHours(1)));
    }

    @Test
    void testBicycleCompatibility() {
        assertTrue(new ParkingSlot("BI1", ParkingSlotType.COMPACT).isCompatible(VehicleType.BICYCLE, base, base.plusHours(1)));
        assertTrue(slot.isCompatible(VehicleType.BICYCLE, base, base.plusHours(1)));
        assertTrue(new ParkingSlot("BI3", ParkingSlotType.LARGE).isCompatible(VehicleType.BICYCLE, base, base.plusHours(1)));
        assertTrue(new ParkingSlot("BI4", ParkingSlotType.HANDICAPPED).isCompatible(VehicleType.BICYCLE, base, base.plusHours(1)));
    }

    @Test
    void testMicrocarCompatibility() {
        assertTrue(new ParkingSlot("MC1", ParkingSlotType.COMPACT).isCompatible(VehicleType.MICROCAR, base, base.plusHours(1)));
        assertTrue(slot.isCompatible(VehicleType.MICROCAR, base, base.plusHours(1)));
        assertFalse(new ParkingSlot("MC3", ParkingSlotType.LARGE).isCompatible(VehicleType.MICROCAR, base, base.plusHours(1)));
        assertFalse(new ParkingSlot("MC4", ParkingSlotType.HANDICAPPED).isCompatible(VehicleType.MICROCAR, base, base.plusHours(1)));
    }

    @Test
    void testTruckCompatibility() {
        ParkingSlot largeSlot = new ParkingSlot("T1", ParkingSlotType.LARGE);
        assertFalse(largeSlot.isCompatible(VehicleType.TRUCK, base, base.plusHours(2)));
        assertFalse(slot.isCompatible(VehicleType.TRUCK, base, base.plusHours(2)));
    }

    @Test
    void testIsAvailableNoBookings() {
        assertTrue(slot.isAvailable(base, base.plusHours(2)));
    }

    @Test
    void testIsAvailableBeforeBooking() {
        slot.getBookings().add(new Booking(1, new Vehicle(1, VehicleType.CAR, 100.0), slot,
                base.plusHours(2), base.plusHours(4), 20.0));
        assertTrue(slot.isAvailable(base, base.plusHours(1).plusMinutes(30)));
    }

    @Test
    void testIsAvailableAdjacentBefore() {
        slot.getBookings().add(new Booking(1, new Vehicle(1, VehicleType.CAR, 100.0), slot,
                base.plusHours(2), base.plusHours(4), 20.0));
        assertTrue(slot.isAvailable(base, base.plusHours(2)));
    }

    @Test
    void testIsAvailableOverlapsStart() {
        slot.getBookings().add(new Booking(1, new Vehicle(1, VehicleType.CAR, 100.0), slot,
                base.plusHours(2), base.plusHours(4), 20.0));
        assertFalse(slot.isAvailable(base.plusHours(1), base.plusHours(2).plusMinutes(30)));
    }

    @Test
    void testIsAvailableExactMatch() {
        slot.getBookings().add(new Booking(1, new Vehicle(1, VehicleType.CAR, 100.0), slot,
                base, base.plusHours(2), 20.0));
        assertFalse(slot.isAvailable(base, base.plusHours(2)));
    }

    @Test
    void testIsAvailableInsideBooking() {
        slot.getBookings().add(new Booking(1, new Vehicle(1, VehicleType.CAR, 100.0), slot,
                base, base.plusHours(4), 40.0));
        assertFalse(slot.isAvailable(base.plusHours(1), base.plusHours(2)));
    }

    @Test
    void testIsAvailableEnclosingBooking() {
        slot.getBookings().add(new Booking(1, new Vehicle(1, VehicleType.CAR, 100.0), slot,
                base.plusHours(1), base.plusHours(2), 10.0));
        assertFalse(slot.isAvailable(base, base.plusHours(3)));
    }

    @Test
    void testIsAvailableOverlapsEnd() {
        slot.getBookings().add(new Booking(1, new Vehicle(1, VehicleType.CAR, 100.0), slot,
                base, base.plusHours(2), 20.0));
        assertFalse(slot.isAvailable(base.plusHours(1).plusMinutes(30), base.plusHours(3)));
    }

    @Test
    void testIsAvailableAdjacentAfter() {
        slot.getBookings().add(new Booking(1, new Vehicle(1, VehicleType.CAR, 100.0), slot,
                base, base.plusHours(2), 20.0));
        assertTrue(slot.isAvailable(base.plusHours(2), base.plusHours(4)));
    }

    @Test
    void testIsAvailableAfterBooking() {
        slot.getBookings().add(new Booking(1, new Vehicle(1, VehicleType.CAR, 100.0), slot,
                base, base.plusHours(2), 20.0));
        assertTrue(slot.isAvailable(base.plusHours(3), base.plusHours(5)));
    }

    @Test
    void testIsAvailableBetweenBookings() {
        Vehicle v = new Vehicle(1, VehicleType.CAR, 100.0);
        slot.getBookings().add(new Booking(1, v, slot, base, base.plusHours(2), 20.0));
        slot.getBookings().add(new Booking(2, v, slot, base.plusHours(4), base.plusHours(6), 20.0));
        assertTrue(slot.isAvailable(base.plusHours(2), base.plusHours(4)));
        assertFalse(slot.isAvailable(base.plusHours(1), base.plusHours(3)));
        assertFalse(slot.isAvailable(base.plusHours(3), base.plusHours(5)));
    }

    @Test
    void testCancelledBookingBlocks() {
        Booking b = new Booking(1, new Vehicle(1, VehicleType.CAR, 100.0), slot, base, base.plusHours(2), 20.0);
        b.cancelBooking();
        slot.getBookings().add(b);
        assertFalse(slot.isAvailable(base, base.plusHours(2)));
    }

    @Test
    void testSlotWalletOperations() {
        assertEquals("S1", slot.getSlotId());
        assertEquals(ParkingSlotType.REGULAR, slot.getSlotType());
        assertEquals(0.0, slot.getBalance(), 1e-6);
        slot.getWallet().addFunds(45.5);
        assertEquals(45.5, slot.getBalance(), 1e-6);
        slot.getWallet().deductFunds(15.5);
        assertEquals(30.0, slot.getBalance(), 1e-6);
    }
}
