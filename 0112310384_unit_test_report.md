# Unit Testing & QA Report: Parking Slot Booking System

## Team Members
* **Student ID:** 0112310384
* **Name:** Omor Faruck Ullas

---

## A) Test Case List

### 1. `WalletTest.java` (Class: `parking.Wallet`)

| Test ID | Class.Method | Why this test? | Verdict | Comments / Observations |
| :--- | :--- | :--- | :---: | :--- |
| **W01** | `Wallet.Wallet()` | Default constructor verification | **PASS** | Balance initializes to `0.0`. |
| **W02** | `Wallet.Wallet(double)` | Parameterized constructor with positive amount | **PASS** | Balance correctly set to `150.0`. |
| **W03** | `Wallet.Wallet(double)` | Boundary: zero initial balance (`0.0`) | **PASS** | Initial zero balance permitted. |
| **W04** | `Wallet.Wallet(double)` | Negative balance boundary check (`-100.0`) | **PASS** | **Defect D1**: Accepts negative balance without validation. |
| **W05** | `Wallet.Wallet(double)` | Fractional negative balance check (`-0.01`) | **PASS** | **Defect D1**: Fractional negative balance permitted. |
| **W06** | `Wallet.Wallet(double)` | Large balance storage check (`1,000,000.0`) | **PASS** | Correctly stored without numerical overflow. |
| **W07** | `Wallet.getBalance()` | Verify balance retrieval | **PASS** | Returns current active balance accurately. |
| **W08** | `Wallet.addFunds(double)` | Positive funds addition | **PASS** | Correctly increments balance (`100 + 50 = 150`). |
| **W09** | `Wallet.addFunds(double)` | Boundary: adding zero amount (`0.0`) | **PASS** | Throws `InvalidAmountException` as required. |
| **W10** | `Wallet.addFunds(double)` | Negative funds addition (`-20.0`) | **PASS** | Throws `InvalidAmountException`. |
| **W11** | `Wallet.addFunds(double)` | Boundary: small negative decimal (`-0.0001`) | **PASS** | Throws `InvalidAmountException`. |
| **W12** | `Wallet.addFunds(double)` | Multiple sequential deposits | **PASS** | Balance correctly accumulates (`10 + 20 + 30`). |
| **W13** | `Wallet.addFunds(double)` | Adding funds to a negative wallet | **PASS** | Balance advances from `-50.0` to `30.0`. |
| **W14** | `Wallet.deductFunds(double)` | Valid deduction less than balance | **PASS** | Balance decrements correctly (`100 - 40 = 60`). |
| **W15** | `Wallet.deductFunds(double)` | Boundary: deduct exact balance (`100.0`) | **PASS** | Balance reaches `0.0` cleanly without error. |
| **W16** | `Wallet.deductFunds(double)` | Deduct amount exceeding balance | **PASS** | Throws `InsufficientFundsException`. |
| **W17** | `Wallet.deductFunds(double)` | Boundary: deduct slightly more (`balance + 0.001`) | **PASS** | Throws `InsufficientFundsException`. |
| **W18** | `Wallet.deductFunds(double)` | Boundary: deduct zero amount (`0.0`) | **PASS** | Throws `InvalidAmountException`. |
| **W19** | `Wallet.deductFunds(double)` | Deduct negative amount (`-20.0`) | **PASS** | Throws `InvalidAmountException`. |
| **W20** | `Wallet.deductFunds(double)` | Deduction from negative balance wallet | **PASS** | Throws `InsufficientFundsException`. |
| **W21** | `Wallet.transferFunds(Wallet, double)` | Valid fund transfer between two wallets | **PASS** | Sender debited (`70.0`), receiver credited (`50.0`). |
| **W22** | `Wallet.transferFunds(Wallet, double)` | Boundary: transfer exact sender balance | **PASS** | Sender reaches `0.0`, receiver credited. |
| **W23** | `Wallet.transferFunds(Wallet, double)` | Transfer amount exceeding sender balance | **PASS** | Throws `InsufficientFundsException`; state unchanged. |
| **W24** | `Wallet.transferFunds(Wallet, double)` | Boundary: transfer zero amount (`0.0`) | **PASS** | Throws `InvalidAmountException`. |
| **W25** | `Wallet.transferFunds(Wallet, double)` | Transfer negative amount (`-15.0`) | **PASS** | Throws `InvalidAmountException`. |
| **W26** | `Wallet.transferFunds(Wallet, double)` | Transfer to `null` target wallet | **PASS** | **Defect D2**: Deducts sender funds before crashing with NPE. |

---

### 2. `VehicleTest.java` (Class: `parking.Vehicle`)

| Test ID | Class.Method | Why this test? | Verdict | Comments / Observations |
| :--- | :--- | :--- | :---: | :--- |
| **V01** | `Vehicle(int, VehicleType, Wallet)` | Full parameter constructor verification | **PASS** | Fields and wallet reference preserved. |
| **V02** | `Vehicle.getBalance()` | Verify balance delegation to wallet | **PASS** | Accurately returns underlying wallet balance. |
| **V03** | `Vehicle(int, VehicleType, double)` | Boundary: zero initial balance (`0.0`) | **PASS** | Internal wallet created with `0.0`. |
| **V04** | `Vehicle(int, VehicleType, double)` | Positive initial balance constructor | **PASS** | Internal wallet created with `200.0`. |
| **V05** | `Vehicle(int, VehicleType, double)` | Negative initial balance check (`-100.0`) | **PASS** | **Defect D3**: Allows negative vehicle balance without validation. |
| **V06** | `Vehicle(int, VehicleType, Wallet)` | Accepts pre-existing negative wallet | **PASS** | Propagates unvalidated negative wallet. |
| **V07** | `Vehicle.getVehicleId()` | Verify ID getter | **PASS** | Returns correct ID. |
| **V08** | `Vehicle.getVehicleType()` | Verify VehicleType getter | **PASS** | Returns correct type (`CAR`). |
| **V09** | `Vehicle.getBalance()` | Live updates to underlying Wallet | **PASS** | External wallet deposit immediately reflects in Vehicle. |
| **V10** | `VehicleType.values()` | Verify all vehicle enum types | **PASS** | Supports CAR, MOTORCYCLE, TRUCK, BICYCLE, MICROCAR, BUS. |
| **V11** | `Vehicle.toString()` | Verify string representation | **PASS** | Output includes ID, type, and wallet balance. |
| **V12** | `Vehicle.getBalance()` | Edge case: Vehicle created with `null` wallet | **PASS** | Documents vulnerability: calling getBalance() throws NPE. |

---

### 3. `ParkingSlotTest.java` (Class: `parking.ParkingSlot`)

| Test ID | Class.Method | Why this test? | Verdict | Comments / Observations |
| :--- | :--- | :--- | :---: | :--- |
| **PS01** | `ParkingSlot(...)` | New slot default active state | **PASS** | Initial `isActive()` is `true`. |
| **PS02** | `ParkingSlot.getBalance()` | Initial slot wallet balance | **PASS** | Initial balance is `0.0`. |
| **PS03** | `ParkingSlot.getBookings()` | New slot bookings collection | **PASS** | Returns empty, non-null list. |
| **PS04** | `activate() / deactivate()` | State toggling mechanism | **PASS** | Active flag flips accurately. |
| **PS05** | `ParkingSlot.isCompatible(...)` | Inactive slot rejection guard | **PASS** | Returns `false` for all requests when inactive. |
| **PS06** | `ParkingSlot.isCompatible(...)` | MOTORCYCLE compatibility matrix | **PASS** | Compatible with COMPACT, REGULAR, LARGE. |
| **PS07** | `ParkingSlot.isCompatible(...)` | CAR compatibility matrix | **PASS** | Compatible with REGULAR, LARGE. |
| **PS08** | `ParkingSlot.isCompatible(...)` | BUS compatibility matrix | **PASS** | Compatible only with LARGE. |
| **PS09** | `ParkingSlot.isCompatible(...)` | BICYCLE compatibility matrix | **PASS** | Compatible with all 4 slot types. |
| **PS10** | `ParkingSlot.isCompatible(...)` | MICROCAR compatibility matrix | **PASS** | Compatible with COMPACT, REGULAR. |
| **PS11** | `ParkingSlot.isCompatible(...)` | TRUCK compatibility check | **PASS** | **Defect D4**: Missing `case TRUCK:` returns `false` for all slots. |
| **PS12** | `ParkingSlot.isAvailable(...)` | Availability on slot with 0 bookings | **PASS** | Returns `true`. |
| **PS13** | `ParkingSlot.isAvailable(...)` | Window completely before existing booking | **PASS** | Returns `true` (no conflict). |
| **PS14** | `ParkingSlot.isAvailable(...)` | Boundary: adjacent before (`end == existingStart`) | **PASS** | Returns `true` (back-to-back allowed). |
| **PS15** | `ParkingSlot.isAvailable(...)` | Window overlaps start of existing booking | **PASS** | Returns `false` (conflict caught). |
| **PS16** | `ParkingSlot.isAvailable(...)` | Window exactly matches existing booking | **PASS** | Returns `false`. |
| **PS17** | `ParkingSlot.isAvailable(...)` | Window completely inside existing booking | **PASS** | Returns `false`. |
| **PS18** | `ParkingSlot.isAvailable(...)` | Window encloses existing booking | **PASS** | Returns `false`. |
| **PS19** | `ParkingSlot.isAvailable(...)` | Window overlaps end of existing booking | **PASS** | Returns `false`. |
| **PS20** | `ParkingSlot.isAvailable(...)` | Boundary: adjacent after (`start == existingEnd`) | **PASS** | Returns `true` (back-to-back allowed). |
| **PS21** | `ParkingSlot.isAvailable(...)` | Window completely after existing booking | **PASS** | Returns `true`. |
| **PS22** | `ParkingSlot.isAvailable(...)` | Fits in gap between two bookings | **PASS** | Available in gap; blocked on overlaps. |
| **PS23** | `ParkingSlot.isAvailable(...)` | Availability after booking cancellation | **PASS** | **Defect D5**: Cancelled booking continues blocking slot. |
| **PS24** | `getSlotId / Type / Balance` | Getters and slot wallet deposit/deduction | **PASS** | ID, type, and wallet operations operate correctly. |

---

### 4. `BookingTest.java` (Class: `parking.Booking`)

| Test ID | Class.Method | Why this test? | Verdict | Comments / Observations |
| :--- | :--- | :--- | :---: | :--- |
| **B01** | `Booking.getBookingStatus()` | Initial booking state | **PASS** | Status starts as `ACTIVE`. |
| **B02** | `Booking.getBookingId()` | Verify ID getter | **PASS** | Returns correct booking ID. |
| **B03** | `Booking.getVehicle()` | Verify Vehicle reference getter | **PASS** | Reference integrity maintained. |
| **B04** | `Booking.getParkingSlot()` | Verify ParkingSlot reference getter | **PASS** | Reference integrity maintained. |
| **B05** | `Booking.getStartTime()` | Verify start time getter | **PASS** | Returns exact start timestamp. |
| **B06** | `Booking.getEndTime()` | Verify end time getter | **PASS** | Returns exact end timestamp. |
| **B07** | `Booking.getAmount()` | Verify amount getter | **PASS** | Returns assigned booking amount (`20.0`). |
| **B08** | `Booking.Booking(...)` | Negative amount constructor check | **PASS** | **Defect D6**: Silently accepts negative amount (`-50.0`). |
| **B09** | `Booking.Booking(...)` | Boundary: zero amount constructor check | **PASS** | **Defect D6**: Silently accepts zero amount (`0.0`). |
| **B10** | `Booking.completeBooking()` | Transition to COMPLETED state | **PASS** | Status changes from `ACTIVE` to `COMPLETED`. |
| **B11** | `Booking.cancelBooking()` | Transition to CANCELLED state | **PASS** | Status changes from `ACTIVE` to `CANCELLED`. |
| **B12** | `Booking.completeBooking()` | State idempotency on complete | **PASS** | Repeated calls remain `COMPLETED`. |
| **B13** | `Booking.cancelBooking()` | State idempotency on cancel | **PASS** | Repeated calls remain `CANCELLED`. |
| **B14** | `Booking.toString()` | Formatted string representation | **PASS** | Contains ID, vehicle, slot, dates, amount, and status. |

---

### 5. `ParkingSystemTest.java` (Class: `parking.ParkingSystem`)

| Test ID | Class.Method | Why this test? | Verdict | Comments / Observations |
| :--- | :--- | :--- | :---: | :--- |
| **SYS01** | `ParkingSystem.getInstance()` | Singleton pattern verification | **PASS** | Always returns identical instance (`assertSame`). |
| **SYS02** | `ParkingSystem.resetForTesting()` | State isolation between unit tests | **PASS** | Clears lists, restores base rate (`10.0`) and wallet. |
| **SYS03** | `addVehicle / setVehicles` | Vehicle registry management | **PASS** | Adding and replacing backing lists verified. |
| **SYS04** | `addParkingSlot / setParkingSlots` | Slot registry management | **PASS** | Adding and replacing backing lists verified. |
| **SYS05** | `setBookings(...)` | Booking collection replacement | **PASS** | Replaces backing booking list cleanly. |
| **SYS06** | `setSYSTEM_WALLET(...)` | System wallet injection | **PASS** | Replaces system wallet and checks balance. |
| **SYS07** | `getAvailableParkingSlots` | Filter slots by vehicle compatibility | **PASS** | Returns REGULAR for CAR, excludes COMPACT. |
| **SYS08** | `getAvailableParkingSlots` | Exclude inactive slots from availability | **PASS** | Deactivated slots filtered out. |
| **SYS09** | `getAvailableParkingSlots` | Exclude occupied slots from availability | **PASS** | Overlapping active bookings filtered out. |
| **SYS10** | `ParkingSystem.book(...)` | Rule: `endTime` strictly before `startTime` | **PASS** | Throws `IllegalBookingTimeException`. |
| **SYS11** | `ParkingSystem.book(...)` | Rule: `endTime` equal to `startTime` | **PASS** | Throws `IllegalBookingTimeException`. |
| **SYS12** | `ParkingSystem.book(...)` | Incompatible vehicle-to-slot booking | **PASS** | Throws `IllegalArgumentException`. |
| **SYS13** | `ParkingSystem.book(...)` | Booking an inactive parking slot | **PASS** | Throws `IllegalArgumentException`. |
| **SYS14** | `ParkingSystem.book(...)` | Nominal successful booking flow | **PASS** | Full fee moved to system wallet, booking registered. |
| **SYS15** | `ParkingSystem.book(...)` | Boundary: vehicle pays exact balance | **PASS** | Booking succeeds; vehicle balance reaches `0.0`. |
| **SYS16** | `ParkingSystem.book(...)` | Booking with zero vehicle balance | **PASS** | Throws `InsufficientFundsException`. |
| **SYS17** | `ParkingSystem.book(...)` | Booking with insufficient vehicle balance | **PASS** | Throws `InsufficientFundsException`. |
| **SYS18** | `ParkingSystem.book(...)` | Booking list state after failed payment | **PASS** | **Defect D8**: Unpaid booking remains in system list. |
| **SYS19** | `ParkingSystem.book(...)` | Pricing: CAR on REGULAR slot (2 hours) | **PASS** | Computes `2 * 10 * 1.0 * 1.0 = 20.0`. |
| **SYS20** | `ParkingSystem.book(...)` | Pricing: MOTORCYCLE on COMPACT slot (2 hours) | **PASS** | Computes `2 * 10 * 0.5 * 0.8 = 8.0`. |
| **SYS21** | `ParkingSystem.book(...)` | Pricing: BICYCLE on REGULAR slot (1 hour) | **PASS** | Computes `1 * 10 * 0.2 * 1.0 = 2.0`. |
| **SYS22** | `ParkingSystem.book(...)` | Pricing: BUS on LARGE slot (3 hours) | **PASS** | Computes `3 * 10 * 2.0 * 1.5 = 90.0`. |
| **SYS23** | `ParkingSystem.book(...)` | Pricing: Custom base rate (`20.0/hr`) | **PASS** | Computes `2 * 20 * 1.0 * 1.0 = 40.0`. |
| **SYS24** | `ParkingSystem.book(...)` | Duration truncation check (90 min) | **PASS** | **Defect D7**: Truncates 1.5h to 1h, charging 10.0 instead of 15.0. |
| **SYS25** | `ParkingSystem.book(...)` | Sub-hour booking crash check (30 min) | **PASS** | **Defect D7**: Truncates to 0h, crashing with `InvalidAmountException`. |
| **SYS26** | `completeBooking(...)` | Successful completion escrow settlement | **PASS** | Slot receives 80% (`16.0`), system retains 20% (`4.0`). |
| **SYS27** | `completeBooking(...)` | Booking status on failed payout transfer | **PASS** | **Defect D9**: Status marked COMPLETED before payout transfer. |
| **SYS28** | `cancelBooking(...)` | Successful cancellation escrow refund | **PASS** | Vehicle refunded 90% (`18.0`), system retains 10% (`2.0`). |
| **SYS29** | `cancelBooking(...)` | Booking status on failed refund transfer | **PASS** | **Defect D10**: Status marked CANCELLED before refund transfer. |
| **SYS30** | `ParkingSystem.book(...)` | Re-booking same slot at overlapping time | **PASS** | Throws `IllegalArgumentException`. |
| **SYS31** | `ParkingSystem.book(...)` | Boundary: adjacent consecutive bookings | **PASS** | Back-to-back bookings succeed without conflict. |
| **SYS32** | `ParkingSystem.book(...)` | Re-booking a slot after cancellation | **PASS** | **Defect D5**: Slot remains blocked despite cancellation. |

---

## B) Defects List

| Defect ID | Class.Method | Defect Description | Suggested Fix |
| :---: | :--- | :--- | :--- |
| **D1** | `Wallet.Wallet(double)` | **Expected:** Constructor should reject negative amounts with `InvalidAmountException`.<br>**Actual:** Accepts negative balance (`-100.0`) without validation. | Add guard: `if (balance < 0) throw new InvalidAmountException();` |
| **D2** | `Wallet.transferFunds(Wallet, double)` | **Expected:** Transfer should validate `toWallet != null` before debiting sender.<br>**Actual:** Debits sender funds first, then crashes on null target with `NullPointerException`, losing funds. | Pre-check target: `if (toWallet == null) throw new IllegalArgumentException("Target cannot be null");` |
| **D3** | `Vehicle.Vehicle(int, VehicleType, double)` | **Expected:** Constructor should reject negative initial balance.<br>**Actual:** Passes negative balance directly into internal wallet without validation. | Add guard: `if (initialBalance < 0) throw new InvalidAmountException();` |
| **D4** | `ParkingSlot.isCompatible(...)` | **Expected:** `TRUCK` is a defined vehicle type (rate 3.0) and should be compatible with LARGE slots.<br>**Actual:** Switch omits `case TRUCK:`, falling through to default `return false;`. | Add `case TRUCK:` into switch statement allowing designated slot types (e.g. `LARGE`). |
| **D5** | `ParkingSlot.isAvailable(...)` | **Expected:** Cancelled bookings should release the slot for future reservations.<br>**Actual:** Overlap loop ignores booking status; cancelled bookings permanently block slot re-use. | Filter active bookings: `if (booking.getBookingStatus() == BookingStatus.ACTIVE && ...)` |
| **D6** | `Booking.Booking(...)` | **Expected:** Constructor should reject non-positive amounts (`amount <= 0`).<br>**Actual:** Accepts zero and negative amounts, crashing downstream escrow settlements. | Add guard: `if (amount <= 0) throw new InvalidAmountException();` |
| **D7** | `ParkingSystem.book(...)` | **Expected:** Fractional durations should be charged proportionally (e.g. 90m = 1.5h).<br>**Actual:** `toHours()` performs integer truncation; sub-hour bookings truncate to 0.0, crashing transfer. | Use minutes: `double hours = Duration.between(startTime, endTime).toMinutes() / 60.0;` |
| **D8** | `ParkingSystem.book(...)` | **Expected:** Bookings should only be recorded after payment transfer succeeds.<br>**Actual:** Booking added to system list before transfer; payment failure leaves an unpaid orphan booking. | Add booking to lists only **after** `vehicle.getWallet().transferFunds(...)` succeeds. |
| **D9** | `ParkingSystem.completeBooking(...)` | **Expected:** Status should transition to COMPLETED only after 80% payout transfer succeeds.<br>**Actual:** Status marked COMPLETED before transfer; transfer failure leaves status corrupted without payout. | Execute `transferFunds(...)` first, and set status to COMPLETED only upon success. |
| **D10** | `ParkingSystem.cancelBooking(...)` | **Expected:** Status should transition to CANCELLED only after 90% refund transfer succeeds.<br>**Actual:** Status marked CANCELLED before refund; transfer failure leaves user unrefunded. | Execute refund `transferFunds(...)` first, and set status to CANCELLED only upon success. |

---

## C) Mutant Analysis

* **Overall Mutation Score:** **90%** (Line Coverage: 98%, Test Strength: 93%)

### 1. Killed Mutant Explanation
* **Location:** `Wallet.java`, Line 20 (`addFunds`)
* **Mutation:** Replaced double addition (`balance += amount`) with subtraction (`balance -= amount`).
* **Explanation:** Caught and killed by `testAddFunds()` in `WalletTest.java`. The test deposits `50.0` into a wallet with `100.0` and asserts the resulting balance equals `150.0`. Under mutation, subtraction produced `50.0`, immediately triggering an `AssertionFailedError` and killing the mutant.

### 2. Surviving Mutant Explanation
* **Location:** `Wallet.java`, Line 39 (`transferFunds`)
* **Mutation:** Changed conditional boundary from `if (amount > 0)` to `if (amount >= 0)`.
* **Explanation:** When `amount = 0.0` is passed, the mutated boundary allows execution to enter `this.deductFunds(0.0)`. However, `deductFunds` contains its own guard `if (amount > 0)` that throws `InvalidAmountException`. Because both the original condition and the mutated path throw `InvalidAmountException`, the external observable behavior remains identical, making this an equivalent mutant that cannot be killed by black-box assertions.

---

## D) Individual Contribution
* **Omor Faruck Ullas (0112310384):** Designed and implemented 108 unit tests across all 5 domain classes (`WalletTest`, `VehicleTest`, `ParkingSlotTest`, `BookingTest`, `ParkingSystemTest`). Conducted defect detection isolating 10 functional and transactional bugs (D1–D10) with proposed fixes. Configured PITest in `pom.xml` across all target classes and performed mutant kill/survival analysis.
