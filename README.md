# 🏛️ Hall Symphony – Hall Booking Management System

> **Java GUI Application** | OOP Assignment | Asia Pacific University
>
> A fully-featured, role-based hall-booking desktop application built with **Java Swing** and flat-file (`.txt`) persistence. No database required.

---

## 📋 Table of Contents

1. [Project Overview](#-project-overview)
2. [Complexity Level Assessment](#-complexity-level-assessment)
3. [Assignment Requirement Coverage](#-assignment-requirement-coverage)
4. [OOP Concepts Demonstrated](#-oop-concepts-demonstrated)
5. [Project Structure](#-project-structure)
6. [Data Files (`.txt` Storage)](#-data-files-txt-storage)
7. [How to Run the Project](#-how-to-run-the-project)
   - [Using the Script (Recommended)](#option-a-using-the-script-recommended)
   - [Manual Step-by-Step](#option-b-manual-step-by-step)
8. [Default Login Credentials](#-default-login-credentials)
9. [User Roles & Features](#-user-roles--features)
10. [Architecture Overview](#-architecture-overview)

---

## 🔍 Project Overview

**Hall Symphony Inc.** operates three types of halls:

| Hall Type     | Capacity | Rate per Hour |
|---------------|----------|---------------|
| Auditorium    | 1,000    | RM 300.00     |
| Banquet Hall  | 300      | RM 100.00     |
| Meeting Room  | 30       | RM 50.00      |

The system supports **four user roles** — Customer, Scheduler (Staff), Administrator (Staff), and Manager (Staff) — each with a dedicated dashboard and tailored feature set. Data is persisted entirely in `.txt` files, as required by the assignment.

---

## 🎯 Complexity Level Assessment

**This project is at an Intermediate level — perfectly appropriate for a 3rd Semester OOP assignment.**

Here is a breakdown of what makes it intermediate (not too basic, not too advanced):

| Concept Used | Level | Notes |
|---|---|---|
| Abstract classes & inheritance | Intermediate | `User` → `Customer`, `Scheduler`, etc. |
| Encapsulation (private fields + getters/setters) | Beginner–Intermediate | Standard OOP practice |
| Polymorphism (role-based dispatch) | Intermediate | `MainFrame.loginSuccess()` switches dashboards by role |
| Interface implementation (`Serializable`) | Intermediate | Marker interface, low complexity |
| Java Swing GUI (`JFrame`, `JPanel`, `CardLayout`) | Intermediate | Industry-standard for Java desktop GUI |
| File I/O (read/write `.txt`) | Intermediate | `BufferedReader`, `PrintWriter`, `FileWriter` |
| Java Streams & Lambdas | Intermediate–Slightly Advanced | Used for filtering, mapping, and collecting |
| `LocalDateTime` and `DateTimeFormatter` | Intermediate | Modern Java 8+ date/time API |
| Service layer separation | Intermediate | Clean separation of concerns |
| Static factory methods (`fromString()`) | Intermediate | Deserialization pattern |

**Verdict:** The use of Java Streams and Lambdas (e.g., `.stream().filter().collect()`) and the Java 8 Date/Time API edges slightly toward the advanced side of intermediate, but these are commonly taught in 2nd–3rd semester OOP courses. All other concepts are solidly at the expected intermediate level. This is **not over-engineered**.

---

## ✅ Assignment Requirement Coverage

Below is a full mapping of every requirement from **Table 2** to the implemented code.

### Role 1: Scheduler
| Requirement | Status | Implemented In |
|---|---|---|
| Login | ✅ | `LoginPanel.java` → `AuthService.login()` |
| Add new hall information | ✅ | `SchedulerDashboard.java` → `HallService.addHall()` |
| View and filter hall information | ✅ | `SchedulerDashboard.java` — table with filter |
| Edit existing hall information | ✅ | `SchedulerDashboard.java` → `HallService.updateHall()` |
| Delete hall information | ✅ | `SchedulerDashboard.java` → `HallService.deleteHall()` |
| Set availability (start/end date & time) | ✅ | `SchedulerDashboard.java` → `HallService.addSchedule()` with type `AVAILABILITY` |
| Set maintenance (start/end date & time) | ✅ | `SchedulerDashboard.java` → `HallService.addSchedule()` with type `MAINTENANCE` |
| Write remarks (optional) | ✅ | `Schedule.remarks` field, stored in `schedules.txt` |
| Logout | ✅ | `BaseDashboard.java` → `frame.logout()` |

### Role 2: Customer
| Requirement | Status | Implemented In |
|---|---|---|
| Registration | ✅ | `RegistrationPanel.java` → `AuthService.registerCustomer()` |
| Login | ✅ | `LoginPanel.java` → `AuthService.login()` |
| Update profile information | ✅ | `CustomerDashboard.java` → `AuthService.updateProfile()` |
| View available halls | ✅ | `CustomerDashboard.java` — queries `HallService` + `Schedule` AVAILABILITY |
| Select preferred hall | ✅ | `CustomerDashboard.java` — hall selection in booking flow |
| Proceed to payment | ✅ | `CustomerDashboard.java` → `BookingService.createBooking()` with `PAID` status |
| View receipt (in JFrame) | ✅ | `CustomerDashboard.java` — receipt shown in a `JOptionPane`/panel after payment |
| View upcoming and past bookings (with filter) | ✅ | `CustomerDashboard.java` → `BookingService.getBookingsByCustomer()` |
| Cancel booking (≥3 days before) | ✅ | `BookingService.cancelBooking()` — enforces 3-day rule |
| Raise issue with manager | ✅ | `CustomerDashboard.java` → `IssueService.raiseIssue()` |
| Logout | ✅ | `BaseDashboard.java` → `frame.logout()` |

### Role 3: Administrator
| Requirement | Status | Implemented In |
|---|---|---|
| Login | ✅ | `LoginPanel.java` → `AuthService.login()` |
| Add scheduler staff | ✅ | `AdminDashboard.java` → `AuthService.addStaff()` with role `Scheduler` |
| View and filter scheduler staff | ✅ | `AdminDashboard.java` — table filtered by role |
| Edit scheduler staff information | ✅ | `AdminDashboard.java` → `AuthService.updateProfile()` |
| Delete scheduler staff | ✅ | `AdminDashboard.java` → `AuthService.deleteUser()` |
| View and filter all users | ✅ | `AdminDashboard.java` → `AuthService.getAllUsers()` |
| Block/Delete user | ✅ | `AdminDashboard.java` → `AuthService.deleteUser()` |
| View and filter all bookings | ✅ | `AdminDashboard.java` → `BookingService.getAllBookings()` |
| Logout | ✅ | `BaseDashboard.java` → `frame.logout()` |

### Role 4: Manager
| Requirement | Status | Implemented In |
|---|---|---|
| Login | ✅ | `LoginPanel.java` → `AuthService.login()` |
| Sales dashboard (weekly/monthly/yearly) | ✅ | `ManagerDashboard.java` → `ReportService.getWeeklySales()`, `.getMonthlySales()`, `.getYearlySales()` |
| View and respond to customer issues | ✅ | `ManagerDashboard.java` → `IssueService.getAllIssues()` |
| Assign scheduler to fix issue | ✅ | `IssueService.updateIssueStatus(issueId, status, assignedSchedulerId)` |
| Change issue status (In Progress / Done / Closed / Cancelled) | ✅ | `Issue.status` field — full lifecycle supported |
| Logout | ✅ | `BaseDashboard.java` → `frame.logout()` |

### General Requirements
| Requirement | Status | Notes |
|---|---|---|
| Compiles and runs without errors | ✅ | Verified via `run.sh` |
| Input validation | ✅ | Business hours (8 AM–6 PM), 3-day cancellation, duplicate username checks, overlap prevention |
| OOP concepts demonstrated | ✅ | See [OOP Concepts](#-oop-concepts-demonstrated) section below |
| `.txt` files used (no database) | ✅ | All data stored in `data/db/*.txt` |
| Written in Java | ✅ | Java 11+ with Swing GUI |

> **Summary: All mandatory requirements from Table 2 are fulfilled. ✅**

---

## 🧱 OOP Concepts Demonstrated

### 1. Encapsulation
Every model class (`User`, `Hall`, `Booking`, `Schedule`, `Issue`) uses **private fields** with public **getters and setters**. Internal data is never exposed directly.

```java
// User.java
private String username;
public String getUsername() { return username; }
public void setUsername(String username) { this.username = username; }
```

### 2. Inheritance
`User` is an **abstract base class**. All four user roles extend it, inheriting common fields (id, username, password, role, fullName, contact) and overriding only what is unique.

```
User (abstract)
├── Customer
├── Scheduler
├── Administrator
└── Manager
```

```java
// Customer.java
public class Customer extends User {
    public Customer(String id, String username, String password, String fullName, String contact) {
        super(id, username, password, "Customer", fullName, contact);
    }
}
```

Similarly, `BaseDashboard` is an **abstract Swing panel** that all four role dashboards (`CustomerDashboard`, `SchedulerDashboard`, `AdminDashboard`, `ManagerDashboard`) extend — reusing the sidebar, header, and logout button layout.

### 3. Polymorphism
`AuthService.getAllUsers()` reads a single `users.txt` file and returns a `List<User>` — but at runtime, each object is the correct concrete type (`Customer`, `Scheduler`, etc.) based on the `role` field. `MainFrame.loginSuccess()` dispatches the correct dashboard using a role switch.

```java
// MainFrame.java — runtime polymorphism
switch (user.getRole()) {
    case "Customer":   dashboard = new CustomerDashboard(...); break;
    case "Scheduler":  dashboard = new SchedulerDashboard(...); break;
    case "Administrator": dashboard = new AdminDashboard(...); break;
    case "Manager":    dashboard = new ManagerDashboard(...); break;
}
```

### 4. Abstraction
- `User` is declared `abstract` — you cannot instantiate a plain `User`, only its concrete subtypes.
- `BaseDashboard` declares `addSidebarButtons(JPanel sidebar)` as an `abstract` method. Each role dashboard **must** implement it, defining its own navigation menu.

### 5. Static Factory Methods (Serialization Pattern)
Every model class that is persisted in `.txt` files implements a `fromString(String data)` static factory method for deserialization. The `toString()` method handles serialization.

```java
// Booking.java
public static Booking fromString(String data) {
    String[] parts = data.split("\\|");
    return new Booking(parts[0], parts[1], parts[2],
        LocalDateTime.parse(parts[3], formatter), ...);
}
```

### 6. Interface Implementation
All model classes implement `java.io.Serializable` — an interface that marks objects as serializable, a standard Java design contract.

### 7. Service Layer Pattern (Separation of Concerns)
Business logic is cleanly separated from UI in dedicated service classes. The UI never reads `.txt` files directly — it always calls a service.

```
UI Layer   →  Service Layer  →  DataStorage (util)  →  .txt files
```

---

## 📁 Project Structure

```
Java_Assignment/
│
├── run.sh                        # Shell script to compile & run (Linux/macOS)
├── sources.txt                   # Auto-generated list of .java files (used by javac)
├── .gitignore
│
├── src/                          # All Java source code
│   └── com/hallsymphony/
│       │
│       ├── model/                # DATA CLASSES (entities / POJOs)
│       │   ├── User.java         # Abstract base class for all users
│       │   ├── Customer.java     # Extends User, role = "Customer"
│       │   ├── Scheduler.java    # Extends User, role = "Scheduler"
│       │   ├── Administrator.java# Extends User, role = "Administrator"
│       │   ├── Manager.java      # Extends User, role = "Manager"
│       │   ├── Hall.java         # Represents a bookable hall
│       │   ├── Booking.java      # Represents a booking transaction
│       │   ├── Schedule.java     # Availability or Maintenance window for a hall
│       │   └── Issue.java        # Customer issue/complaint
│       │
│       ├── service/              # BUSINESS LOGIC LAYER
│       │   ├── AuthService.java  # Login, register, add/delete/update users
│       │   ├── HallService.java  # CRUD for halls + schedule management
│       │   ├── BookingService.java  # Create, cancel, update bookings (with validation)
│       │   ├── IssueService.java # Raise and update issue status
│       │   └── ReportService.java  # Sales aggregation (weekly/monthly/yearly)
│       │
│       ├── ui/                   # GRAPHICAL USER INTERFACE (Java Swing)
│       │   ├── MainFrame.java    # Application window, CardLayout navigation
│       │   ├── LoginPanel.java   # Login screen
│       │   ├── RegistrationPanel.java  # Customer self-registration screen
│       │   ├── BaseDashboard.java      # Abstract dashboard (sidebar + header layout)
│       │   ├── CustomerDashboard.java  # Full customer feature set
│       │   ├── SchedulerDashboard.java # Hall + schedule management features
│       │   ├── AdminDashboard.java     # Staff + user + booking management
│       │   └── ManagerDashboard.java   # Sales reports + issue management
│       │
│       └── util/                 # UTILITIES
│           ├── DataStorage.java  # Reads/writes .txt files (the "database layer")
│           ├── DataInitializer.java  # Seeds default data on first run
│           └── StyleConfig.java  # Centralized colours & fonts for the UI
│
├── bin/                          # Compiled .class files (auto-generated, do not edit)
│
└── data/
    └── db/                       # FLAT-FILE "DATABASE"
        ├── users.txt             # All users (customers + staff)
        ├── halls.txt             # Hall definitions
        ├── schedules.txt         # Availability + maintenance schedules
        ├── bookings.txt          # All bookings (auto-created when first booking is made)
        └── issues.txt            # Customer issues/complaints
```

---

## 🗃️ Data Files (`.txt` Storage)

All data is stored in `data/db/`. Each file uses a **pipe-delimited (`|`)** format — one record per line. This replaces a traditional database as required by the assignment.

### `users.txt`
Stores all users — customers and staff alike — distinguished by the `role` field.

**Format:** `id|username|password|role|fullName|contact`

**Example:**
```
ADM1|admin|admin123|Administrator|System Admin|0123456789
MAN2|manager|manager123|Manager|Chief Manager|0123456788
SCH3|staff|staff123|Scheduler|Lead Scheduler|0123456787
CUST4|john|pass123|Customer|John Doe|0123456786
```

---

### `halls.txt`
Stores hall definitions.

**Format:** `id|name|type|capacity|ratePerHour|description`

**Example:**
```
HALL1|Grand Auditorium|Auditorium|1000|300.0|Large hall for conferences
HALL2|Royal Banquet|Banquet Hall|300|100.0|Perfect for weddings
HALL3|Meeting Room A|Meeting Room|30|50.0|Small room for discussions
```

---

### `schedules.txt`
Stores both **availability windows** (set by Scheduler for customers to book) and **maintenance windows** (during which booking is blocked).

**Format:** `id|hallId|startTime|endTime|type|remarks`

**Example:**
```
SCH1|HALL1|2024-07-01T08:00|2024-07-31T18:00|AVAILABILITY|July Slots
SCH2|HALL1|2024-08-01T10:00|2024-08-01T14:00|MAINTENANCE|Air conditioning check
```

---

### `bookings.txt`
Stores all booking transactions. Created automatically when the first booking is made.

**Format:** `id|customerId|hallId|startTime|endTime|totalPrice|status|remarks`

- `status` values: `PENDING`, `PAID`, `CANCELLED`

**Example:**
```
BOOK1|CUST4|HALL1|2024-07-15T09:00|2024-07-15T12:00|900.0|PAID|Annual conference
BOOK2|CUST4|HALL2|2024-07-20T14:00|2024-07-20T17:00|300.0|CANCELLED|
```

---

### `issues.txt`
Stores customer-raised issues linked to a specific booking.

**Format:** `id|customerId|bookingId|description|status|assignedTo`

- `status` values: `OPEN`, `IN_PROGRESS`, `DONE`, `CLOSED`, `CANCELLED`
- `assignedTo` is a Scheduler's ID, or `NONE` if unassigned

**Example:**
```
ISSUE1|CUST4|BOOK1|A/C not working in Grand Auditorium|IN_PROGRESS|SCH3
```

---

## ▶️ How to Run the Project

### Prerequisites

- **Java Development Kit (JDK) 11 or higher** must be installed.
- Verify with: `java -version` and `javac -version`

---

### Option A: Using the Script (Recommended)

The `run.sh` script automates compiling and launching the application in one step.

**Step 1:** Open a terminal and navigate to the project root:
```bash
cd /path/to/Java_Assignment
```

**Step 2:** Make the script executable (only needed once):
```bash
chmod +x run.sh
```

**Step 3:** Run the script:
```bash
./run.sh
```

**What the script does (internally):**
1. Creates the `bin/` directory if it does not exist.
2. Finds all `.java` source files and writes their paths to `sources.txt`.
3. Compiles everything with `javac -d bin @sources.txt`.
4. If compilation succeeds, launches the app with `java -cp bin com.hallsymphony.ui.MainFrame`.

**Why the script is needed:** Java requires a two-step process — compile (`javac`) then run (`java`). The project has 25 source files across 4 packages. Finding all files and passing them to `javac` manually is tedious and error-prone. The script eliminates this with `find src -name "*.java"`.

---

### Option B: Manual Step-by-Step

If you cannot use the script (e.g., on Windows without Git Bash), run each command separately.

**Step 1:** Open a terminal and navigate to the project root.

**Step 2:** Create the `bin/` output directory:
```bash
mkdir -p bin
```

**Step 3:** Compile all Java files:
```bash
find src -name "*.java" > sources.txt
javac -d bin @sources.txt
```

> **On Windows (Command Prompt)**, replace the `find` command with:
> ```cmd
> dir /s /b src\*.java > sources.txt
> javac -d bin @sources.txt
> ```

**Step 4:** Run the application:
```bash
java -cp bin com.hallsymphony.ui.MainFrame
```

**Step 5 (Optional):** To clean compiled files and recompile fresh:
```bash
rm -rf bin/
mkdir bin
```

---

### Why must you run from the project root directory?

The `DataStorage.java` utility resolves data files relative to the **current working directory**:
```java
private static final String BASE_PATH = "data/db/";
```

If you run `java` from inside the `src/` or `bin/` folder, the application will not find `data/db/` and will silently create empty data. **Always run from the project root.**

---

## 🔑 Default Login Credentials

These accounts are automatically created by `DataInitializer.java` the first time the application is launched (when `data/db/users.txt` is empty).

| Role          | Username  | Password     |
|---------------|-----------|--------------|
| Administrator | `admin`   | `admin123`   |
| Manager       | `manager` | `manager123` |
| Scheduler     | `staff`   | `staff123`   |
| Customer      | *(self-register via the Registration screen)* | |

> ⚠️ **Note:** Passwords are stored in plain text in `users.txt` as this is an academic project with no database. In a production system, passwords would always be hashed (e.g., bcrypt).

---

## 👤 User Roles & Features

### 🗓️ Scheduler
- Add, view, edit, and delete hall records.
- Set **availability windows** (date + time range) so customers can book.
- Set **maintenance windows** (date + time range) to block booking during repairs.
- Write optional remarks for both types of schedules.

### 👥 Customer
- Self-register and log in.
- Update profile (full name, contact number).
- Browse halls available within scheduler-set windows.
- Book a hall (validated against: business hours 8 AM–6 PM, no overlap with other bookings or maintenance).
- Pay and receive a printed receipt.
- View upcoming and past bookings, with filters.
- Cancel a booking — **only if cancellation is ≥3 days before the event start date**.
- Raise an issue/complaint linked to a specific booking.

### 🛡️ Administrator
- Manage Scheduler staff accounts (add, view, edit, delete).
- View all users (customers + staff) with filtering.
- Block or delete any user account.
- View all bookings across all customers with filtering.

### 📊 Manager
- View a **sales dashboard** with totals for:
  - Current Week
  - Current Month
  - Current Year
- View all customer-raised issues.
- Assign a Scheduler to resolve an issue.
- Update issue status through its full lifecycle:
  - `OPEN` → `IN_PROGRESS` → `DONE` → `CLOSED` (or `CANCELLED`)

---

## 🏛️ Architecture Overview

```
┌──────────────────────────────────────────────────────────────┐
│                     UI Layer (Swing)                         │
│  MainFrame → LoginPanel / RegistrationPanel                 │
│           → CustomerDashboard / SchedulerDashboard          │
│           → AdminDashboard / ManagerDashboard               │
│           (all dashboards extend BaseDashboard)             │
└──────────────────────┬───────────────────────────────────────┘
                       │ calls
┌──────────────────────▼───────────────────────────────────────┐
│                  Service Layer                               │
│  AuthService │ HallService │ BookingService                  │
│  IssueService │ ReportService                               │
└──────────────────────┬───────────────────────────────────────┘
                       │ calls
┌──────────────────────▼───────────────────────────────────────┐
│               Utility Layer                                  │
│  DataStorage (read/write .txt files)                        │
│  DataInitializer (seeds default data on first run)          │
│  StyleConfig (centralised UI colours & fonts)               │
└──────────────────────┬───────────────────────────────────────┘
                       │ persists
┌──────────────────────▼───────────────────────────────────────┐
│                  Data Layer (Flat Files)                     │
│  data/db/users.txt │ halls.txt │ schedules.txt              │
│  data/db/bookings.txt │ issues.txt                          │
└──────────────────────────────────────────────────────────────┘
```

---

## 📝 Notes for the Report

- **Use Case Diagram**: Should show 4 actors (Customer, Scheduler, Administrator, Manager) with their respective use cases mapped to the features in Table 2.
- **Class Diagram**: Should show the `User` hierarchy, `Hall`, `Booking`, `Schedule`, `Issue`, and the service/utility classes with their relationships (association, dependency, inheritance).
- **OOP Justification**: Reference the six OOP concepts documented in the [OOP Concepts](#-oop-concepts-demonstrated) section, with specific class and method names.
- **Appendix**: Include screenshots of all five `.txt` files from `data/db/` (as required by Section 7.1.F).

---

*Hall Symphony Inc. — Hall Booking Management System | Asia Pacific University | OOP Assignment*
