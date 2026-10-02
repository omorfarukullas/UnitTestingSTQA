# Unit Testing & QA Report: Parking Slot Booking System

## 0) Team Members
* **Student ID:** 0112310384
* **Name:** [Omor Faruck Ullas]

---

## A) Test Case List

### 1. `WalletTest.java` (Class: `parking.Wallet`)

| Test ID | Class.Method | Why this test? | Verdict | Comments / Observations |
| :--- | :--- | :--- | :---: | :--- |
| **W01** | `Wallet.Wallet()` | Default constructor sets balance to 0.0 | **PASS** | Validates baseline zero initial balance. |
| **W02** | `Wallet.Wallet(double)` | Parameterized constructor with positive balance | **PASS** | Valid balance 150.0 assigned accurately. |
| **W03** | `Wallet.Wallet(double)` | Parameterized constructor with zero balance (0.0) | **PASS** | Boundary: zero initial balance permitted. |
| **W04** | `Wallet.Wallet(double)` | Parameterized constructor with negative balance (`-100.0`) | **PASS** | **Defect D1**: Constructor lacks negative check; silently creates wallet with negative balance without throwing `InvalidAmountException`. |
| **W05** | `Wallet.Wallet(double)` | Parameterized constructor with negative decimal (`-0.01`) | **PASS** | **Defect D1**: Fractional negative balance accepted without validation. |
| **W06** | `Wallet.Wallet(double)` | Parameterized constructor with large balance (`1,000,000.0`) | **PASS** | Large amounts stored accurately without overflow. |
| **W07** | `Wallet.getBalance()` | Verifies getter returns current balance | **PASS** | Confirms balance state retrieval. |
| **W08** | `Wallet.addFunds(double)` | Positive amount addition increases balance | **PASS** | 100.0 + 50.0 = 150.0. |
| **W09** | `Wallet.addFunds(double)` | Adding zero (`0.0`) amount throws exception | **PASS** | Boundary: throws `InvalidAmountException` (amount must be > 0). |
| **W10** | `Wallet.addFunds(double)` | Adding negative amount (`-20.0`) throws exception | **PASS** | Throws `InvalidAmountException`. |
| **W11** | `Wallet.addFunds(double)` | Adding small negative decimal (`-0.0001`) | **PASS** | Boundary: throws `InvalidAmountException`. |
| **W12** | `Wallet.addFunds(double)` | Sequential multiple additions accumulate balance | **PASS** | Verifies iterative accumulation (10 + 20 + 30). |
| **W13** | `Wallet.addFunds(double)` | Adding funds to negative balance wallet | **PASS** | Balance advances from -50.0 to 30.0. |
| **W14** | `Wallet.deductFunds(double)` | Deduct amount less than balance | **PASS** | 100.0 - 40.0 = 60.0. |
| **W15** | `Wallet.deductFunds(double)` | Deduct exact balance (`amount == balance`) | **PASS** | Boundary: balance becomes exactly 0.0 without throwing. |
| **W16** | `Wallet.deductFunds(double)` | Deduct amount exceeding balance | **PASS** | Throws `InsufficientFundsException`. |
| **W17** | `Wallet.deductFunds(double)` | Deduct amount slightly exceeding balance (`balance + 0.001`) | **PASS** | Boundary: throws `InsufficientFundsException`. |
| **W18** | `Wallet.deductFunds(double)` | Deduct zero (`0.0`) amount | **PASS** | Boundary: throws `InvalidAmountException`. |
| **W19** | `Wallet.deductFunds(double)` | Deduct negative amount (`-20.0`) | **PASS** | Throws `InvalidAmountException`. |
| **W20** | `Wallet.deductFunds(double)` | Deduct from negative-balance wallet | **PASS** | Throws `InsufficientFundsException`. |
| **W21** | `Wallet.transferFunds(Wallet, double)` | Valid transfer debits sender and credits receiver | **PASS** | Sender: 100 -> 70, Receiver: 20 -> 50. |
| **W22** | `Wallet.transferFunds(Wallet, double)` | Transfer exact sender balance | **PASS** | Boundary: sender becomes 0.0, receiver credited. |
| **W23** | `Wallet.transferFunds(Wallet, double)` | Transfer amount exceeding sender balance | **PASS** | Throws `InsufficientFundsException`. |
| **W24** | `Wallet.transferFunds(Wallet, double)` | Transfer zero (`0.0`) amount | **PASS** | Boundary: throws `InvalidAmountException`. |
| **W25** | `Wallet.transferFunds(Wallet, double)` | Transfer negative amount (`-15.0`) | **PASS** | Throws `InvalidAmountException`. |
| **W26** | `Wallet.transferFunds(Wallet, double)` | Transfer to `null` receiver debits sender before crashing | **PASS** | **Defect D2**: Atomicity violation: sender is debited before checking if receiver is null, crashing with `NullPointerException` and losing funds. |

---

### 2. `VehicleTest.java` (Class: `parking.Vehicle`)

| Test ID | Class.Method | Why this test? | Verdict | Comments / Observations |
| :--- | :--- | :--- | :---: | :--- |
| **V01** | `Vehicle.Vehicle(int, VehicleType, Wallet)` | Constructor sets all fields; getWallet returns same instance | **PASS** | Reference preservation verified. |
| **V02** | `Vehicle.getBalance()` | getBalance reflects provided Wallet balance | **PASS** | Direct delegation to wallet confirmed. |
| **V03** | `Vehicle.Vehicle(int, VehicleType, double)` | Constructor with 0.0 initial balance | **PASS** | Boundary: zero initial balance permitted. |
| **V04** | `Vehicle.Vehicle(int, VehicleType, double)` | Constructor creates internal wallet with positive balance | **PASS** | Internal Wallet instance correctly instantiated with 200.0. |
| **V05** | `Vehicle.Vehicle(int, VehicleType, double)` | Constructor accepts negative `initialBalance` | **PASS** | **Defect D3**: Vehicle accepts negative initial balance (`-100.0`) without validation. |
| **V06** | `Vehicle.Vehicle(int, VehicleType, Wallet)` | Constructor accepts Wallet with negative balance | **PASS** | Propagates unvalidated negative wallet into domain entity. |
| **V07** | `Vehicle.getVehicleId()` | Returns ID passed to constructor | **PASS** | Verifies ID getter. |
| **V08** | `Vehicle.getVehicleType()` | Returns VehicleType passed to constructor | **PASS** | Type getter verified. |
| **V09** | `Vehicle.getBalance()` | Live updates to underlying Wallet reflect in Vehicle | **PASS** | Shared reference mutation verification (100 -> 130). |
| **V10** | `VehicleType.values()` | Every enum value usable in Vehicle | **PASS** | CAR, MOTORCYCLE, TRUCK, BICYCLE, MICROCAR, BUS all supported. |
| **V11** | `Vehicle.toString()` | Formatted string output contains essential fields | **PASS** | Contains vehicleId, vehicleType, walletBalance. |
| **V12** | `Vehicle.getBalance()` | Null wallet parameter causes NullPointerException on getBalance() | **PASS** | Documents vulnerability when constructed with null wallet. |

---

### 3. `ParkingSlotTest.java` (Class: `parking.ParkingSlot`)

| Test ID | Class.Method | Why this test? | Verdict | Comments / Observations |
| :--- | :--- | :--- | :---: | :--- |
| **PS01** | `ParkingSlot.ParkingSlot(...)` | New slot active by default | **PASS** | `isActive()` is true on instantiation. |
| **PS02** | `ParkingSlot.getBalance()` | Slot wallet initialized to 0.0 | **PASS** | Initial slot balance is 0.0. |
| **PS03** | `ParkingSlot.getBookings()` | New slot bookings list is empty | **PASS** | Returns non-null, empty list. |
| **PS04** | `ParkingSlot.activate/deactivate` | State toggling between active and inactive | **PASS** | `deactivate()` -> false, `activate()` -> true. |
| **PS05** | `ParkingSlot.isCompatible(...)` | Inactive slot rejected for all vehicle types/times | **PASS** | Pre-condition guard in `isCompatible` returns false when inactive. |
| **PS06** | `ParkingSlot.isCompatible(...)` | MOTORCYCLE compatibility matrix | **PASS** | Allowed: COMPACT, REGULAR, LARGE. Incompatible: HANDICAPPED. |
| **PS07** | `ParkingSlot.isCompatible(...)` | CAR compatibility matrix | **PASS** | Allowed: REGULAR, LARGE. Incompatible: COMPACT, HANDICAPPED. |
| **PS08** | `ParkingSlot.isCompatible(...)` | BUS compatibility matrix | **PASS** | Allowed: LARGE. Incompatible: COMPACT, REGULAR, HANDICAPPED. |
| **PS09** | `ParkingSlot.isCompatible(...)` | BICYCLE compatibility matrix | **PASS** | Allowed in all four slot types. |
| **PS10** | `ParkingSlot.isCompatible(...)` | MICROCAR compatibility matrix | **PASS** | Allowed: COMPACT, REGULAR. Incompatible: LARGE, HANDICAPPED. |
| **PS11** | `ParkingSlot.isCompatible(...)` | TRUCK compatibility check | **PASS** | **Defect D4**: `case TRUCK:` is missing in switch statement, permanently returning false for all slots. |
| **PS12** | `ParkingSlot.isAvailable(...)` | Free slot with no bookings returns true | **PASS** | Available when booking list is empty. |
| **PS13** | `ParkingSlot.isAvailable(...)` | Window completely before existing booking | **PASS** | Available without conflict. |
| **PS14** | `ParkingSlot.isAvailable(...)` | Window adjacent before existing (`newEnd == existingStart`) | **PASS** | Boundary: Available (`startTime < endTime`). |
| **PS15** | `ParkingSlot.isAvailable(...)` | Window overlaps start of existing booking | **PASS** | Blocked (returns false). |
| **PS16** | `ParkingSlot.isAvailable(...)` | Window exactly matches existing booking | **PASS** | Blocked (returns false). |
| **PS17** | `ParkingSlot.isAvailable(...)` | Window completely inside existing booking | **PASS** | Blocked (returns false). |
| **PS18** | `ParkingSlot.isAvailable(...)` | Window encloses existing booking | **PASS** | Blocked (returns false). |
| **PS19** | `ParkingSlot.isAvailable(...)` | Window overlaps end of existing booking | **PASS** | Blocked (returns false). |
| **PS20** | `ParkingSlot.isAvailable(...)` | Window adjacent after existing (`newStart == existingEnd`) | **PASS** | Boundary: Available (`endTime > startTime`). |
| **PS21** | `ParkingSlot.isAvailable(...)` | Window completely after existing booking | **PASS** | Available without conflict. |
| **PS22** | `ParkingSlot.isAvailable(...)` | Fits in gap between two bookings | **PASS** | Available in gap; blocked on overlaps. |
| **PS23** | `ParkingSlot.isAvailable(...)` | Cancelled booking availability check | **PASS** | **Defect D5**: `isAvailable` ignores `bookingStatus`; cancelled bookings permanently block slot re-use. |
| **PS24** | `ParkingSlot.getSlotId/Type` | Slot ID and SlotType getters and wallet operations | **PASS** | Balance reflects credits and debits accurately. |

---

### 4. `BookingTest.java` (Class: `parking.Booking`)

| Test ID | Class.Method | Why this test? | Verdict | Comments / Observations |
| :--- | :--- | :--- | :---: | :--- |
| **B01** | `Booking.getBookingStatus()` | Initial booking status is ACTIVE | **PASS** | Verifies default lifecycle state. |
| **B02** | `Booking.getBookingId()` | Returns booking ID passed to constructor | **PASS** | Verifies ID getter. |
| **B03** | `Booking.getVehicle()` | Returns Vehicle instance passed to constructor | **PASS** | Reference integrity preserved. |
| **B04** | `Booking.getParkingSlot()` | Returns ParkingSlot instance passed to constructor | **PASS** | Reference integrity preserved. |
| **B05** | `Booking.getStartTime()` | Returns start time passed to constructor | **PASS** | Temporal field getter verified. |
| **B06** | `Booking.getEndTime()` | Returns end time passed to constructor | **PASS** | Temporal field getter verified. |
| **B07** | `Booking.getAmount()` | Returns monetary amount passed to constructor | **PASS** | Fee getter verified. |
| **B08** | `Booking.Booking(...)` | Constructor accepts negative amount | **PASS** | **Defect D6**: Negative amount accepted without validation. |
| **B09** | `Booking.Booking(...)` | Constructor accepts zero amount | **PASS** | **Defect D6**: Zero amount accepted without validation. |
| **B10** | `Booking.completeBooking()` | Status transitions from ACTIVE to COMPLETED | **PASS** | State machine transition verified. |
| **B11** | `Booking.cancelBooking()` | Status transitions from ACTIVE to CANCELLED | **PASS** | State machine transition verified. |
| **B12** | `Booking.completeBooking()` | Calling completeBooking multiple times remains COMPLETED | **PASS** | Idempotency verified. |
| **B13** | `Booking.cancelBooking()` | Calling cancelBooking multiple times remains CANCELLED | **PASS** | Idempotency verified. |
| **B14** | `Booking.toString()` | Formatted string output contains essential fields | **PASS** | Contains ID, vehicle, slot, start, end, amount, status. |

---

### 5. `ParkingSystemTest.java` (Class: `parking.ParkingSystem`)

| Test ID | Class.Method | Why this test? | Verdict | Comments / Observations |
| :--- | :--- | :--- | :---: | :--- |
| **SYS01** | `ParkingSystem.getInstance()` | Singleton pattern returns same instance | **PASS** | `assertSame(a, b)` verified. |
| **SYS02** | `ParkingSystem.resetForTesting()` | Clears vehicles, slots, bookings, rate, wallet | **PASS** | Global state reset for test isolation. |
| **SYS03** | `ParkingSystem.addVehicle/setVehicles` | Vehicle list registration and replacement | **PASS** | Backing list mutations verified. |
| **SYS04** | `ParkingSystem.addParkingSlot/setParkingSlots` | ParkingSlot list registration and replacement | **PASS** | Backing list mutations verified. |
| **SYS05** | `ParkingSystem.setBookings(...)` | Booking list replacement | **PASS** | Backing list mutations verified. |
| **SYS06** | `ParkingSystem.setSYSTEM_WALLET(...)` | Custom system wallet replacement and balance | **PASS** | System wallet reference verified. |
| **SYS07** | `ParkingSystem.getAvailableParkingSlots` | Filters slots by vehicle compatibility | **PASS** | Only compatible slots returned. |
| **SYS08** | `ParkingSystem.getAvailableParkingSlots` | Excludes inactive slots | **PASS** | Inactive slots omitted from results. |
| **SYS09** | `ParkingSystem.getAvailableParkingSlots` | Excludes occupied slots | **PASS** | Overlapping slots omitted from results. |
| **SYS10** | `ParkingSystem.book(...)` | `endTime` before `startTime` throws exception | **PASS** | Throws `IllegalBookingTimeException`. |
| **SYS11** | `ParkingSystem.book(...)` | `endTime` equal to `startTime` throws exception | **PASS** | Throws `IllegalBookingTimeException`. |
| **SYS12** | `ParkingSystem.book(...)` | Incompatible slot booking throws exception | **PASS** | Throws `IllegalArgumentException`. |
| **SYS13** | `ParkingSystem.book(...)` | Inactive slot booking throws exception | **PASS** | Throws `IllegalArgumentException`. |
| **SYS14** | `ParkingSystem.book(...)` | Successful booking lifecycle and fee transfer | **PASS** | Vehicle charged, system credited, booking added. |
| **SYS15** | `ParkingSystem.book(...)` | Booking with exact vehicle balance succeeds | **PASS** | Boundary: vehicle balance reaches 0.0. |
| **SYS16** | `ParkingSystem.book(...)` | Booking with zero vehicle balance throws exception | **PASS** | Throws `InsufficientFundsException`. |
| **SYS17** | `ParkingSystem.book(...)` | Booking with insufficient vehicle funds throws exception | **PASS** | Throws `InsufficientFundsException`. |
| **SYS18** | `ParkingSystem.book(...)` | Failed payment leaves orphan booking in system | **PASS** | **Defect D8**: Booking added to system list before payment succeeds. |
| **SYS19** | `ParkingSystem.book(...)` | Pricing: CAR on REGULAR slot (2 hours) | **PASS** | 2 * 10 * 1.0 * 1.0 = 20.0. |
| **SYS20** | `ParkingSystem.book(...)` | Pricing: MOTORCYCLE on COMPACT slot (2 hours) | **PASS** | 2 * 10 * 0.5 * 0.8 = 8.0. |
| **SYS21** | `ParkingSystem.book(...)` | Pricing: BICYCLE on REGULAR slot (1 hour) | **PASS** | 1 * 10 * 0.2 * 1.0 = 2.0. |
| **SYS22** | `ParkingSystem.book(...)` | Pricing: BUS on LARGE slot (3 hours) | **PASS** | 3 * 10 * 2.0 * 1.5 = 90.0. |
| **SYS23** | `ParkingSystem.book(...)` | Pricing: Custom base hourly rate | **PASS** | Rate updated to 20.0; fee computes accurately. |
| **SYS24** | `ParkingSystem.book(...)` | Integer truncation on fractional hour booking (90 min) | **PASS** | **Defect D7**: Duration.toHours() truncates 1.5h to 1h, undercharging customer. |
| **SYS25** | `ParkingSystem.book(...)` | Sub-hour booking crashes system (30 min) | **PASS** | **Defect D7**: Duration.toHours() truncates to 0h; transferFunds(0) crashes with `InvalidAmountException`. |
| **SYS26** | `ParkingSystem.completeBooking` | Successful completion splits fee 80% slot, 20% system | **PASS** | Slot receives 16.0, system retains 4.0. |
| **SYS27** | `ParkingSystem.completeBooking` | Failed payout transfer corrupts booking status | **PASS** | **Defect D9**: Status changed to COMPLETED before money transfer occurs. |
| **SYS28** | `ParkingSystem.cancelBooking` | Successful cancellation refunds 90% vehicle, 10% system | **PASS** | Vehicle refunded 498.0, system retains 2.0. |
| **SYS29** | `ParkingSystem.cancelBooking` | Failed refund transfer corrupts booking status | **PASS** | **Defect D10**: Status changed to CANCELLED before money transfer occurs. |
| **SYS30** | `ParkingSystem.book(...)` | Double booking same slot in overlapping time throws | **PASS** | Throws `IllegalArgumentException`. |
| **SYS31** | `ParkingSystem.book(...)` | Adjacent consecutive bookings permitted | **PASS** | Boundary: back-to-back bookings allowed without conflict. |
| **SYS32** | `ParkingSystem.book(...)` | Re-booking a slot after cancellation | **PASS** | **Defect D5**: Cancelled booking blocks subsequent bookings. |

---

## B) Defects List

| Defect ID | Class.Method | Description | Suggested Fix |
| :---: | :--- | :--- | :--- |
| **D1** | `Wallet.Wallet(double)` | Parameterized constructor accepts negative balance without throwing `InvalidAmountException`. | Add parameter validation guard: `if (balance < 0) throw new InvalidAmountException();` |
| **D2** | `Wallet.transferFunds(Wallet, double)` | Deducts money from sender before validating that `toWallet` is not null. When `toWallet` is null, sender funds are permanently lost before `NullPointerException` is thrown. | Add null check at the start: `if (toWallet == null) throw new IllegalArgumentException("Target wallet cannot be null");` |
| **D3** | `Vehicle.Vehicle(int, VehicleType, double)` | Constructor does not validate `initialBalance` and passes negative values directly to `Wallet`, allowing vehicles with negative balances to exist. | Validate in constructor: `if (initialBalance < 0) throw new InvalidAmountException();` before initializing internal wallet. |
| **D4** | `ParkingSlot.isCompatible(...)` | Switch statement omits `case TRUCK:`. TRUCK is a valid vehicle type with rate 3.0, but falls to `default: return false;`, making trucks incompatible with all slots. | Add `case TRUCK:` into the switch statement allowing compatible slots (e.g., `LARGE`). |
| **D5** | `ParkingSlot.isAvailable(...)` | Overlap check does not verify `booking.getBookingStatus()`. Cancelled bookings permanently block the slot from being re-booked for that time window. | Add status filter: `if (booking.getBookingStatus() == BookingStatus.ACTIVE && ...)` |
| **D6** | `Booking.Booking(...)` | Constructor accepts negative and zero amounts without validation. Downstream operations later crash with `InvalidAmountException` during transfers. | Add guard in constructor: `if (amount <= 0) throw new InvalidAmountException();` |
| **D7** | `ParkingSystem.book(...)` | `Duration.between(...).toHours()` performs integer truncation. A 90-minute booking is charged as only 1 hour, and sub-hour bookings (<60 min) truncate to 0 hours, crashing transfers with `InvalidAmountException`. | Use fractional hours: `double hours = Duration.between(startTime, endTime).toMinutes() / 60.0;` or enforce a minimum 1-hour charge. |
| **D8** | `ParkingSystem.book(...)` | `bookings.add(booking)` executes before payment transfer. If payment fails (e.g. insufficient funds), an unpaid orphan booking remains in the system's active list. | Move `bookings.add(booking)` and `slot.getBookings().add(booking)` to run only **after** `transferFunds` succeeds. |
| **D9** | `ParkingSystem.completeBooking(...)` | `booking.completeBooking()` changes status to COMPLETED before the 80% payout transfer. If the transfer fails, status remains corrupted as COMPLETED without slot payout. | Execute `transferFunds(...)` first, and call `booking.completeBooking()` only upon transfer success. |
| **D10** | `ParkingSystem.cancelBooking(...)` | `booking.cancelBooking()` changes status to CANCELLED before the 90% refund transfer. If refund transfer fails, status remains CANCELLED without refunding the vehicle. | Execute refund `transferFunds(...)` first, and call `booking.cancelBooking()` only upon transfer success. |

---

## C) Mutant Analysis

* **Overall Mutation Score:** **90%** (Line Coverage: 98%, Test Strength: 93%)

### 1. Killed Mutant Explanation
* **Location:** `Wallet.java`, Line 20 (`addFunds`)
* **Mutation:** Replaced double addition (`balance += amount`) with subtraction (`balance -= amount`).
* **Explanation:** This mutant was killed by `testAddFunds()` in `WalletTest.java`. The test begins with a wallet containing `100.0`, deposits `50.0`, and asserts that the resulting balance equals `150.0`. Under the mutated code, the balance evaluated to `50.0`, causing `assertEquals(150.0, balance)` to fail immediately with an `AssertionFailedError`, successfully detecting and killing the mutant.

### 2. Surviving Mutant Explanation
* **Location:** `Wallet.java`, Line 39 (`transferFunds`)
* **Mutation:** Changed conditional boundary from `if (amount > 0)` to `if (amount >= 0)`.
* **Explanation:** When `amount = 0.0` is passed, the mutated boundary condition allows execution to proceed into `this.deductFunds(0.0)`. However, `deductFunds(amount)` has its own check `if (amount > 0)` which throws `InvalidAmountException` when `amount == 0.0`. Because both the original condition and the mutated path ultimately result in an `InvalidAmountException` being thrown, the external observable behavior of the method remains identical for all test inputs, making this an equivalent mutant that cannot be killed by black-box assertions.

---

## D) Individual Contribution
* **0112310384:** Designed and implemented comprehensive test suites across all 5 classes (`WalletTest`, `VehicleTest`, `ParkingSlotTest`, `BookingTest`, and `ParkingSystemTest`) covering 108 test cases. Conducted systematic defect analysis uncovering defects D1 through D10 across domain rules, pricing models, and transaction atomicity flows. Configured PITest mutation analysis in `pom.xml` and analyzed mutant behavior.
