package parking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    private Booking b;
    private Vehicle v;
    private ParkingSlot slot;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.of(2025, 1, 1, 10, 0);
        end = LocalDateTime.of(2025, 1, 1, 12, 0);
        v = new Vehicle(1, VehicleType.CAR, 500.0);
        slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        b = new Booking(1, v, slot, start, end, 20.0);
    }

    @Test
    void testInitialStatus() {
        assertEquals(BookingStatus.ACTIVE, b.getBookingStatus());
    }

    @Test
    void testGetBookingId() {
        assertEquals(1, b.getBookingId());
    }

    @Test
    void testGetVehicle() {
        assertSame(v, b.getVehicle());
    }

    @Test
    void testGetParkingSlot() {
        assertSame(slot, b.getParkingSlot());
    }

    @Test
    void testGetStartTime() {
        assertEquals(start, b.getStartTime());
    }

    @Test
    void testGetEndTime() {
        assertEquals(end, b.getEndTime());
    }

    @Test
    void testGetAmount() {
        assertEquals(20.0, b.getAmount(), 1e-6);
    }

    @Test
    void testNegativeAmount() {
        Booking negBooking = new Booking(2, v, slot, start, end, -50.0);
        assertEquals(-50.0, negBooking.getAmount(), 1e-6);
    }

    @Test
    void testZeroAmount() {
        Booking zeroBooking = new Booking(3, v, slot, start, end, 0.0);
        assertEquals(0.0, zeroBooking.getAmount(), 1e-6);
    }

    @Test
    void testCompleteBooking() {
        b.completeBooking();
        assertEquals(BookingStatus.COMPLETED, b.getBookingStatus());
    }

    @Test
    void testCancelBooking() {
        b.cancelBooking();
        assertEquals(BookingStatus.CANCELLED, b.getBookingStatus());
    }

    @Test
    void testCompleteBookingIdempotent() {
        b.completeBooking();
        b.completeBooking();
        assertEquals(BookingStatus.COMPLETED, b.getBookingStatus());
    }

    @Test
    void testCancelBookingIdempotent() {
        b.cancelBooking();
        b.cancelBooking();
        assertEquals(BookingStatus.CANCELLED, b.getBookingStatus());
    }

    @Test
    void testToString() {
        String str = b.toString();
        assertNotNull(str);
        assertTrue(str.contains("1"));
        assertTrue(str.contains("20.0"));
        assertTrue(str.contains("ACTIVE"));
    }
}
