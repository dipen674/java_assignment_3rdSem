# 🏛️ Hall Symphony — Hall Booking Management System

> **Java GUI Desktop Application** | 3rd Semester OOP Assignment | Group Project
>
> A fully-featured, role-based hall-booking desktop application built with **Java Swing** and flat-file (`.txt`) persistence. No external database required.

---

## 📋 Table of Contents

1. [Project Overview](#-project-overview)
2. [How to Run the Project](#-how-to-run-the-project)
3. [Default Login Credentials](#-default-login-credentials)
4. [Project Structure](#-project-structure)
5. [Detailed File-by-File Documentation](#-detailed-file-by-file-documentation)
   - [Model Layer (Data Classes)](#-model-layer--data-classes)
   - [Service Layer (Business Logic)](#-service-layer--business-logic)
   - [UI Layer (Graphical User Interface)](#-ui-layer--graphical-user-interface)
   - [Utility Layer (Helpers)](#-utility-layer--helpers)
6. [Data Files (.txt Storage)](#-data-files-txt-storage)
7. [User Roles & Features](#-user-roles--features)
8. [OOP Concepts Demonstrated](#-oop-concepts-demonstrated)
9. [Assignment Requirement Coverage](#-assignment-requirement-coverage)
10. [Architecture Overview](#-architecture-overview)
11. [Complexity Level Assessment](#-complexity-level-assessment)
12. [Notes for the Report](#-notes-for-the-report)

---

## 🔍 Project Overview

**Hall Symphony Inc.** operates daily from **8:00 AM to 6:00 PM** and offers three types of halls for booking:

| Hall Type     | Capacity | Rate per Hour |
|---------------|----------|---------------|
| Auditorium    | 1,000    | RM 300.00     |
| Banquet Hall  | 300      | RM 100.00     |
| Meeting Room  | 30       | RM 50.00      |

The system supports **four user roles** — Customer, Scheduler (Staff), Administrator (Staff), and Manager (Staff) — each with a dedicated dashboard and tailored feature set. Data is persisted entirely in `.txt` files, as required by the assignment.

### What this application does (in simple terms):

1. **Customers** can register, log in, browse available halls, book them for events (weddings, conferences, meetings), pay, get a receipt, view their past bookings, cancel bookings, and report issues.
2. **Schedulers** (staff) manage the halls — they add, edit, and delete hall records, set when halls are available for booking, and mark maintenance periods.
3. **Administrators** (staff) manage all user accounts — they add new scheduler staff, view/edit/delete any user, and see all bookings across the system.
4. **Managers** (staff) see sales analytics (weekly/monthly/yearly revenue) and handle customer complaints by assigning schedulers to fix issues.

---

## ▶️ How to Run the Project

### Prerequisites

- **Java Development Kit (JDK) 11 or higher** must be installed.
- Verify by opening a terminal/command prompt and typing:
  ```
  java -version
  javac -version
  ```
  Both commands should show version 11 or higher.

> ⚠️ **IMPORTANT:** You must always run all commands from the **project root directory** (the folder that contains `src/`, `data/`, and `run.sh`). If you run from a different folder, the application will not find the data files.

---

### Option A: Using a Script (Recommended — One Command!)

#### 🐧 Linux / macOS
```bash
cd /path/to/Java_Assignment
chmod +x run.sh       # Only needed the first time
./run.sh
```

#### 🪟 Windows — Command Prompt
```cmd
cd C:\path\to\Java_Assignment
run.bat
```
Or simply **double-click** `run.bat` in File Explorer!

Both scripts automatically:
1. Create the `bin/` output directory if it doesn't exist
2. Find all `.java` source files
3. Compile them with `javac`
4. Launch the application with `java`

---

### Option B: Manual Compilation & Run (All Platforms)

Use this method if the scripts don't work on your system.

#### 🪟 Windows — Command Prompt (cmd)

```cmd
cd C:\path\to\Java_Assignment
mkdir bin
dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt
java -cp bin com.hallsymphony.ui.MainFrame
```

#### 🪟 Windows — PowerShell

```powershell
cd C:\path\to\Java_Assignment
New-Item -ItemType Directory -Force -Path bin
Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName } | Out-File -Encoding ascii sources.txt
javac -d bin "@sources.txt"
java -cp bin com.hallsymphony.ui.MainFrame
```

#### 🍎 macOS / 🐧 Linux — Terminal

```bash
cd /path/to/Java_Assignment
mkdir -p bin
find src -name "*.java" > sources.txt
javac -d bin @sources.txt
java -cp bin com.hallsymphony.ui.MainFrame
```

---

### 🔄 To Recompile After Code Changes

If you make changes to the source code, just repeat the **compile** and **run** commands. You do not need to delete the `bin` folder.

### Why must you run from the project root directory?

The `DataStorage.java` utility resolves data files relative to the **current working directory**:
```java
private static final String BASE_PATH = "data/db/";
```
If you run `java` from inside the `src/` or `bin/` folder, the application will not find `data/db/` and will silently create empty data. **Always run from the project root.**

---

## 🔑 Default Login Credentials

These accounts are pre-loaded in `data/db/users.txt`.

| Role          | Username   | Password      | Full Name           |
|---------------|------------|---------------|---------------------|
| Administrator | `admin`    | `admin123`    | Deependra Adhikari  |
| Manager       | `manager`  | `manager123`  | Rajesh Hamal        |
| Scheduler     | `staff`    | `staff123`    | Suman Shrestha      |
| Scheduler     | `bikash`   | `bikash123`   | Bikash Tamang       |
| Scheduler     | `ramesh`   | `ramesh123`   | Ramesh Adhikari     |
| Customer      | `dipen`    | `dipen123`    | Dipen Bhatta        |
| Customer      | `dixita`   | `dixita123`   | Dixita Sharma       |
| Customer      | `anchita`  | `anchita123`  | Anchita Gurung      |
| Customer      | `priya`    | `priya123`    | Priya Maharjan      |
| Customer      | `aarav`    | `aarav123`    | Aarav Thapa         |
| Customer      | `sunita`   | `sunita123`   | Sunita Rai          |
| Customer      | `bishal`   | `bishal123`   | Bishal Bhattarai    |
| Customer      | `nisha`    | `nisha123`    | Nisha Poudel        |

> 💡 New customers can also self-register via the Registration screen.

> ⚠️ **Note:** Passwords are stored in plain text in `users.txt` as this is an academic project. In a production system, passwords would always be hashed (e.g., bcrypt).

---

## 📁 Project Structure

```
Java_Assignment/
│
├── run.sh                        # Shell script to compile & run (Linux/macOS)
├── run.bat                       # Batch script to compile & run (Windows)
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
        ├── bookings.txt          # All bookings
        └── issues.txt            # Customer issues/complaints
```

---

## 📝 Detailed File-by-File Documentation

This section explains **every single file** in the project — what it does, what fields and methods it has, and how it connects to other parts of the system. If you've never seen this code before, read this section carefully.

---

### 📦 Model Layer — Data Classes

The `model/` package contains the **data classes** (also called entities or POJOs). These represent the core "things" in our system: users, halls, bookings, schedules, and issues. Every model class:
- Uses **private fields** (encapsulation)
- Has **public getters and setters**
- Implements `java.io.Serializable` (a marker interface)
- Has a `toString()` method that converts the object to a **pipe-delimited string** for saving to `.txt` files
- Has a `fromString(String data)` static method that converts a pipe-delimited string **back** into an object

---

#### 📄 `User.java` — The Abstract Base Class

**Purpose:** This is the **parent class** for all user types. It is declared `abstract`, which means you **cannot create a User object directly** — you must create a Customer, Scheduler, Administrator, or Manager instead.

**Fields (all `private`):**
| Field | Type | Description |
|-------|------|-------------|
| `id` | `String` | Unique identifier (e.g., `CUST5`, `ADM1`, `SCH3`) |
| `username` | `String` | Login username (e.g., `dipen`, `admin`) |
| `password` | `String` | Login password (stored in plain text for this assignment) |
| `role` | `String` | The user's role: `"Customer"`, `"Scheduler"`, `"Administrator"`, or `"Manager"` |
| `fullName` | `String` | Full name (e.g., `Dipen Bhatta`) |
| `contact` | `String` | Phone number (e.g., `9822611896`) |

**Key Methods:**
- `toString()` → Converts user to pipe-delimited string: `ADM1|admin|admin123|Administrator|Deependra Adhikari|9841234567`
- All getters and setters (e.g., `getUsername()`, `setFullName(String)`, etc.)

**OOP Concept:** Abstraction (abstract class), Encapsulation (private fields + public getters/setters)

---

#### 📄 `Customer.java`

**Purpose:** Represents a customer who books halls. Extends `User` and sets `role = "Customer"` automatically.

```java
public class Customer extends User {
    public Customer(String id, String username, String password, String fullName, String contact) {
        super(id, username, password, "Customer", fullName, contact);
    }
}
```

**Key Methods:**
- `fromString(String data)` → Reads a pipe-delimited line and creates a `Customer` object if `role == "Customer"`

**OOP Concept:** Inheritance — `Customer` inherits all 6 fields and methods from `User`

---

#### 📄 `Scheduler.java`

**Purpose:** Represents a staff member who manages halls and schedules. Extends `User` and sets `role = "Scheduler"`.

**Identical structure to `Customer.java`** but sets the role to `"Scheduler"`. The `fromString()` method checks that `parts[3].equals("Scheduler")` to avoid creating the wrong type.

---

#### 📄 `Administrator.java`

**Purpose:** Represents a staff member who manages user accounts. Extends `User` and sets `role = "Administrator"`.

**Same structure** as Customer/Scheduler with role = `"Administrator"`.

---

#### 📄 `Manager.java`

**Purpose:** Represents a staff member who views sales stats and manages issues. Extends `User` and sets `role = "Manager"`.

**Same structure** as Customer/Scheduler with role = `"Manager"`.

---

#### 📄 `Hall.java`

**Purpose:** Represents a hall that customers can book.

**Fields (all `private`):**
| Field | Type | Description |
|-------|------|-------------|
| `id` | `String` | Unique ID (e.g., `HALL1`) |
| `name` | `String` | Hall display name (e.g., `Everest Grand Auditorium`) |
| `type` | `String` | One of: `"Auditorium"`, `"Banquet Hall"`, `"Meeting Room"` |
| `capacity` | `int` | Maximum number of people (e.g., `1000`) |
| `ratePerHour` | `double` | Booking price per hour in RM (e.g., `300.0`) |
| `description` | `String` | A short text describing the hall |

**Key Methods:**
- `toString()` → `HALL1|Everest Grand Auditorium|Auditorium|1000|300.0|Prestigious auditorium for conferences`
- `fromString(String)` → Parses a line from `halls.txt` into a `Hall` object

---

#### 📄 `Booking.java`

**Purpose:** Represents a single booking/reservation made by a customer.

**Fields (all `private`):**
| Field | Type | Description |
|-------|------|-------------|
| `id` | `String` | Unique ID (e.g., `BOOK1`) |
| `customerId` | `String` | The ID of the customer who made the booking (e.g., `CUST5`) |
| `hallId` | `String` | The booked hall (e.g., `HALL1`) |
| `startTime` | `LocalDateTime` | When the event starts (e.g., `2024-07-10T09:00:00`) |
| `endTime` | `LocalDateTime` | When the event ends (e.g., `2024-07-10T13:00:00`) |
| `totalPrice` | `double` | Price in RM calculated as `hours × ratePerHour` |
| `status` | `String` | `"PENDING"`, `"PAID"`, or `"CANCELLED"` |
| `remarks` | `String` | Event description (e.g., `Nepal IT Conference 2024`) |

**Key Methods:**
- Uses `DateTimeFormatter.ISO_LOCAL_DATE_TIME` for date conversion
- `fromString()` parses dates from strings like `2024-07-10T09:00:00`

---

#### 📄 `Schedule.java`

**Purpose:** Represents a time window for a hall — either **AVAILABILITY** (when customers can book) or **MAINTENANCE** (when the hall is closed for repairs).

**Fields (all `private`):**
| Field | Type | Description |
|-------|------|-------------|
| `id` | `String` | Unique ID (e.g., `SCH1`) |
| `hallId` | `String` | Which hall this schedule applies to (e.g., `HALL1`) |
| `startTime` | `LocalDateTime` | Start of the window |
| `endTime` | `LocalDateTime` | End of the window |
| `type` | `String` | `"AVAILABILITY"` or `"MAINTENANCE"` |
| `remarks` | `String` | Optional notes (e.g., `Air conditioning servicing`) |

**How it works:**
- A **Scheduler** creates an `AVAILABILITY` schedule: "HALL1 is available from July 1 to August 31"
- A **Scheduler** creates a `MAINTENANCE` schedule: "HALL1 is closed for repairs on August 5"
- **Customers** can only book halls that have an `AVAILABILITY` schedule covering their desired dates
- **Bookings** cannot overlap with `MAINTENANCE` periods

---

#### 📄 `Issue.java`

**Purpose:** Represents a complaint or problem reported by a customer about a past booking.

**Fields (all `private`):**
| Field | Type | Description |
|-------|------|-------------|
| `id` | `String` | Unique ID (e.g., `ISSUE1`) |
| `customerId` | `String` | Who reported it (e.g., `CUST5`) |
| `bookingId` | `String` | Which booking it relates to (e.g., `BOOK1`) |
| `description` | `String` | Description of the problem |
| `status` | `String` | `"OPEN"` → `"IN_PROGRESS"` → `"DONE"` → `"CLOSED"` (or `"CANCELLED"`) |
| `assignedTo` | `String` | Scheduler staff ID assigned to fix it, or `null`/`"NONE"` |

---

### ⚙️ Service Layer — Business Logic

The `service/` package contains the **business logic**. These classes sit between the UI and the data files. The UI **never** reads `.txt` files directly — it always calls a service method. All methods in service classes are `static` (you call them like `AuthService.login(...)` without creating an object).

---

#### 📄 `AuthService.java` — User Authentication & Management

**Purpose:** Handles everything related to user accounts: login, registration, adding staff, updating profiles, and deleting users.

**Data file:** `users.txt`

**Methods:**

| Method | What it does |
|--------|-------------|
| `getAllUsers()` | Reads `users.txt`, parses each line, and returns a `List<User>`. It uses a `switch` statement on the `role` field to create the correct subtype (`Customer`, `Scheduler`, `Administrator`, or `Manager`). This is **polymorphism** in action. |
| `login(username, password)` | Searches all users for one matching both username AND password. Returns `Optional<User>` — empty if not found. |
| `registerCustomer(username, password, fullName, contact)` | Creates a new `Customer` with auto-generated ID (e.g., `CUST14`). Checks for duplicate usernames first. Appends to `users.txt`. |
| `addStaff(username, password, fullName, contact, role)` | Same as register, but for staff. Creates `Scheduler`, `Administrator`, or `Manager` based on the `role` parameter. Used by the Admin dashboard. |
| `updateProfile(userId, fullName, contact)` | Finds a user by ID, updates their name and contact, then **rewrites the entire `users.txt` file** with the updated data. |
| `deleteUser(userId)` | Removes a user from the list and rewrites `users.txt`. |
| `getNextUserId(prefix)` | **Private helper.** Scans all existing user IDs to find the highest number, then returns `maxNumber + 1`. This prevents ID collisions after deletions. |

**How `getAllUsers()` demonstrates polymorphism (important for the report):**
```java
switch (role) {
    case "Customer":      user = Customer.fromString(line); break;
    case "Scheduler":     user = Scheduler.fromString(line); break;
    case "Administrator": user = Administrator.fromString(line); break;
    case "Manager":       user = Manager.fromString(line); break;
}
```
Even though the return type is `List<User>`, each item in the list is actually a `Customer`, `Scheduler`, etc.

---

#### 📄 `HallService.java` — Hall & Schedule Management

**Purpose:** Manages hall records and their availability/maintenance schedules.

**Data files:** `halls.txt`, `schedules.txt`

**Hall Methods:**

| Method | What it does |
|--------|-------------|
| `getAllHalls()` | Reads `halls.txt` and returns `List<Hall>`. Uses Java Streams: `.stream().map(Hall::fromString).filter(Objects::nonNull).collect(...)` |
| `addHall(name, type, capacity, rate, description)` | Creates a new `Hall` with auto-generated ID and appends to `halls.txt` |
| `updateHall(updatedHall)` | Finds and replaces the matching hall in the list, then rewrites `halls.txt` |
| `deleteHall(hallId)` | Removes the hall and rewrites `halls.txt` |

**Schedule Methods:**

| Method | What it does |
|--------|-------------|
| `getAllSchedules()` | Reads `schedules.txt` |
| `getSchedulesByHall(hallId)` | Returns only schedules for a specific hall |
| `addSchedule(schedule)` | Adds a new schedule. **If the type is `MAINTENANCE`, it checks for time overlap with existing maintenance windows** and rejects if there's a conflict. |
| `deleteSchedule(scheduleId)` | Deletes a schedule |

---

#### 📄 `BookingService.java` — Booking & Payment Logic

**Purpose:** Handles creating, cancelling, and updating bookings. This is the most **validation-heavy** service.

**Data file:** `bookings.txt`

**Methods:**

| Method | What it does |
|--------|-------------|
| `getAllBookings()` | Reads and returns all bookings |
| `getBookingsByCustomer(customerId)` | Filters bookings for a specific customer |
| `createBooking(customerId, hallId, start, end, price, remarks)` | **The core booking method.** Performs **4 validations** (see below) before creating the booking. |
| `cancelBooking(bookingId)` | Cancels a booking, but **only if it is at least 3 days before the event start**. Sets status to `"CANCELLED"`. |
| `updateBookingStatus(bookingId, status)` | General-purpose status update |

**The 4 validation checks in `createBooking()`:**

1. **Start before End** — `start` must be before `end`
2. **Business hours** — Start cannot be before 8 AM, end cannot be after 6 PM
3. **No overlap with existing bookings** — Checks that no other non-cancelled booking for the same hall overlaps with the requested time
4. **No overlap with maintenance** — Checks that the requested time doesn't fall within a `MAINTENANCE` schedule
5. **Within availability** — Checks that the requested time falls within an `AVAILABILITY` schedule set by the scheduler

If all validations pass, the booking is created with status `"PAID"` (payment is assumed at booking time).

---

#### 📄 `IssueService.java` — Issue/Complaint Management

**Purpose:** Handles customer complaints linked to specific bookings.

**Data file:** `issues.txt`

**Methods:**

| Method | What it does |
|--------|-------------|
| `getAllIssues()` | Reads all issues |
| `getIssuesByCustomer(customerId)` | Filters issues for a specific customer |
| `raiseIssue(customerId, bookingId, description)` | Creates a new issue with status `"OPEN"` and `assignedTo = null` |
| `updateIssueStatus(issueId, status, assignedTo)` | Updates the status and optionally assigns a Scheduler to fix the issue |

**Issue lifecycle:**
```
OPEN → IN_PROGRESS → DONE → CLOSED
                   ↘ CANCELLED
```

---

#### 📄 `ReportService.java` — Sales Analytics

**Purpose:** Calculates sales totals for the Manager's dashboard.

**Methods:**

| Method | What it does |
|--------|-------------|
| `getSalesForPeriod(start, end)` | Sums `totalPrice` of all `"PAID"` bookings within a date range |
| `getWeeklySales()` | Returns sales for the last 7 days |
| `getMonthlySales()` | Returns sales for the last 30 days |
| `getYearlySales()` | Returns sales for the last 365 days |

Uses Java Streams: `.stream().filter().filter().mapToDouble().sum()`

---

### 🖥️ UI Layer — Graphical User Interface

The `ui/` package contains the **Java Swing** GUI code. Every screen the user sees is defined here.

---

#### 📄 `MainFrame.java` — The Application Window

**Purpose:** This is the **entry point** of the entire application. It creates the main window and manages screen transitions.

**How it works:**
- Creates a `JFrame` (the main window) with size `1100 × 750`
- Uses `CardLayout` to switch between "cards" (screens): Login → Register → Dashboard
- On startup, shows the Login screen
- When login succeeds, calls `loginSuccess(User)` which detects the user's role and creates the correct dashboard

**Key method — `loginSuccess(User user)`:**
```java
switch (user.getRole()) {
    case "Customer":      dashboard = new CustomerDashboard(this, (Customer) user); break;
    case "Scheduler":     dashboard = new SchedulerDashboard(this, (Scheduler) user); break;
    case "Administrator": dashboard = new AdminDashboard(this, (Administrator) user); break;
    case "Manager":       dashboard = new ManagerDashboard(this, (Manager) user); break;
}
```
This is **polymorphism** — the same `loginSuccess` method handles all four user types differently.

**The `main()` method:**
```java
public static void main(String[] args) {
    StyleConfig.applyGlobalTheme();          // 1. Apply UI theme
    DataInitializer.initialize();             // 2. Seed default data
    SwingUtilities.invokeLater(() -> {
        new MainFrame().setVisible(true);    // 3. Show window
    });
}
```

---

#### 📄 `LoginPanel.java` — Login Screen

**Purpose:** Beautiful login form with username/password fields, sign-in button, and a "Register here" link.

**UI Elements:**
- Card-style container centered on screen
- 🏛 emoji icon as branding
- "Hall Symphony" title
- Username text field (with styled border and label)
- Password field
- "Sign In" button (blue, styled)
- "Don't have an account? Register here" link (navigates to RegistrationPanel)

**Validation:**
- Checks that both fields are non-empty
- Calls `AuthService.login(username, password)` — shows error dialog if login fails

**UX Feature:** Pressing **Enter** in the password field triggers login (no need to click the button).

---

#### 📄 `RegistrationPanel.java` — Customer Registration Screen

**Purpose:** Allows new customers to create an account.

**UI Elements:**
- 📝 icon
- "Create Account" title
- Four fields: Full Name, Username, Password, Contact Number
- "Create Account" button (green, success-styled)
- "Already have an account? Sign in" link (navigates back to login)

**Validation:**
- All four fields must be non-empty
- Calls `AuthService.registerCustomer()` — shows error if username already exists

---

#### 📄 `BaseDashboard.java` — Abstract Dashboard Layout

**Purpose:** Defines the **common layout** shared by all four dashboards. This is an **abstract class** — you cannot create it directly, only through its subclasses.

**Layout structure (from left to right and top to bottom):**
```
┌──────────┬──────────────────────────────────────────────┐
│          │        HEADER (Welcome back, Dipen)          │
│ SIDEBAR  ├──────────────────────────────────────────────┤
│          │                                              │
│  🏛 Logo │          CONTENT AREA                        │
│          │   (changes based on sidebar button clicks)   │
│  Nav     │                                              │
│  Links   │                                              │
│          │                                              │
│  ───     │                                              │
│  User    │                                              │
│  Info    │                                              │
│ [Logout] │                                              │
└──────────┴──────────────────────────────────────────────┘
```

**Sidebar contains:**
1. 🏛 Hall Symphony logo
2. "NAVIGATION" label
3. Role-specific menu buttons (defined by each subclass via `addSidebarButtons()`)
4. User info (name + role)
5. Red "Sign Out" button

**Abstract method:** `addSidebarButtons(JPanel sidebar)` — each dashboard subclass **must** implement this to define its own menu items.

**OOP Concepts:** Abstraction (abstract class), Inheritance (all dashboards inherit this layout)

---

#### 📄 `CustomerDashboard.java` — Customer Features

**Purpose:** Full feature set for customers.

**Sidebar buttons (tabs):**
| Tab | Card Name | What it shows |
|-----|-----------|---------------|
| Available Halls | `BROWSE` | Table of halls that have AVAILABILITY schedules set by the scheduler. Customer can select and book. |
| My Bookings | `BOOKINGS` | Filterable table of all bookings (All/Upcoming/Past/CANCELLED/PAID). Cancel button. |
| Report Issue | `ISSUES` | View existing issues + "Report New Issue" button (enter booking ID + description). |
| My Profile | `PROFILE` | View and edit full name and contact. Persisted to file. |

**Key features:**
- **Booking flow:** Select hall → Enter date/time → System validates and calculates price → Creates booking → Shows receipt in a dialog
- **Receipt display:** After booking, a formatted receipt popup shows: receipt number, customer name, hall name, date/time, duration, rate, subtotal, and total price
- **Booking filter:** ComboBox filter with options: All, Upcoming, Past, CANCELLED, PAID
- **Cancellation rule:** Must be ≥3 days before the event. If too late, error shown.

---

#### 📄 `SchedulerDashboard.java` — Hall & Schedule Management

**Purpose:** For scheduler staff to manage halls and set availability/maintenance.

**Sidebar buttons (tabs):**
| Tab | Card Name | What it shows |
|-----|-----------|---------------|
| Manage Halls | `HALLS` | Table of all halls with search filter, Add/Edit/Delete buttons |
| Hall Scheduling | `SCHEDULING` | Select hall → View/Add/Delete availability and maintenance schedules |
| Maintenance | `MAINTENANCE` | View issues assigned to this scheduler |

**Key features:**
- **Hall filter** — Search by name or type
- **Add/Edit Hall form** — Popup dialog with fields: Name, Type (dropdown), Capacity, Rate
- **Schedule form** — Select hall, enter start/end datetime, choose AVAILABILITY or MAINTENANCE, add optional remarks

---

#### 📄 `AdminDashboard.java` — User & Staff Management

**Purpose:** For administrators to manage all user accounts and view bookings.

**Sidebar buttons (tabs):**
| Tab | Card Name | What it shows |
|-----|-----------|---------------|
| Staff Management | `STAFF` | Filterable table of Scheduler staff only. Add/Edit/Delete buttons. |
| User Management | `USERS` | Filterable table of ALL users (any role). Delete button. |
| Central Bookings | `BOOKINGS` | Read-only table of all bookings across all customers. |

**Key features:**
- **Staff filter** — Search by name or username
- **Add Staff form** — Popup with fields: Username, Password, Name, Contact. Always creates role "Scheduler".
- **Edit Staff form** — Same popup, pre-filled with existing data
- **User filter** — Search all users by name or username
- **Delete User** — Removes selected user from `users.txt`

---

#### 📄 `ManagerDashboard.java` — Sales & Issue Management

**Purpose:** For managers to view revenue analytics and handle customer complaints.

**Sidebar buttons (tabs):**
| Tab | Card Name | What it shows |
|-----|-----------|---------------|
| Sales Stats | `SALES` | Three coloured stat cards: Weekly, Monthly, Yearly sales totals |
| Maintenance | `MAINTENANCE` | Filterable table of all issues + Assign Scheduler + Update Status buttons |

**Key features:**
- **Sales cards** — Show values with green text on white cards with blue borders
- **Issue filter** — Search issues by description text
- **Assign Scheduler** — Select an issue → Enter scheduler ID → Assigns and sets status to `IN_PROGRESS`
- **Update Status** — Dropdown with options: `IN_PROGRESS`, `DONE`, `CLOSED`, `CANCELLED`

---

### 🔧 Utility Layer — Helpers

---

#### 📄 `DataStorage.java` — The "Database" Engine

**Purpose:** This is the **core file I/O utility** that all services use to read and write `.txt` files. It acts as a simple replacement for a real database.

**Key constant:** `BASE_PATH = "data/db/"` — all files are relative to this path.

**Methods:**

| Method | What it does |
|--------|-------------|
| `readList(fileName)` | Opens `data/db/<fileName>`, reads every non-empty line, returns `List<String>` |
| `saveList(fileName, list)` | **Overwrites** the entire file with the `toString()` of each item in the list. Used for updates and deletes. |
| `appendToFile(fileName, obj)` | **Appends** a single line (`obj.toString()`) to the end of the file. Used for inserts. |

**Important distinction:**
- `appendToFile` → Used when **adding** a new record (fast, doesn't rewrite the whole file)
- `saveList` → Used when **updating or deleting** a record (rewrites the entire file since we can't edit individual lines)

---

#### 📄 `DataInitializer.java` — First-Run Data Seeder

**Purpose:** On the very first run (when `data/db/` is empty), this class creates default staff accounts, halls, schedules, and a sample issue.

**What it creates on first run:**
1. `data/db/` directory (if it doesn't exist)
2. Default users: 1 Admin, 1 Manager, 1 Scheduler
3. Default halls: Grand Auditorium, Royal Banquet, Meeting Room A
4. Default schedules: July availability for all 3 halls
5. Default issue: Sample A/C complaint

> **Note:** Since we manually populated the data files with Nepal-themed data, the initializer only runs if the files are empty.

---

#### 📄 `StyleConfig.java` — UI Theme & Design System

**Purpose:** Centralized design system — all colours, fonts, and common UI component styling methods are defined here. This ensures the entire application looks **consistent and professional**.

**Color Palette:**
| Name | Hex | Used for |
|------|-----|----------|
| `PRIMARY_COLOR` | `#2563EB` (Vibrant Blue) | Buttons, links, highlights |
| `PRIMARY_DARK` | `#1D4ED8` (Darker Blue) | Button hover states |
| `PRIMARY_LIGHT` | `#DBEAFE` (Light Blue) | Selected table rows, role badges |
| `SECONDARY_COLOR` | `#0F172A` (Slate-900) | Sidebar background |
| `ACCENT_COLOR` | `#DC2626` (Red) | Logout/danger buttons |
| `SUCCESS_COLOR` | `#16A34A` (Green) | Register button, sales figures |
| `BACKGROUND_COLOR` | `#F1F5F9` (Slate-100) | Page background |
| `CARD_COLOR` | `#FFFFFF` (White) | Cards, panels, headers |
| `TEXT_COLOR` | `#0F172A` (Slate-900) | Primary text |
| `TEXT_SECONDARY` | `#64748B` (Slate-500) | Labels, subtitles |
| `BORDER_COLOR` | `#E2E8F0` (Slate-200) | Borders, separators |

**Fonts:**
| Name | Style | Used for |
|------|-------|----------|
| `TITLE_FONT` | SansSerif Bold 28pt | Login title, page titles |
| `HEADER_FONT` | SansSerif Bold 18pt | Section headers |
| `NORMAL_FONT` | SansSerif Plain 15pt | Body text, buttons, labels |
| `SMALL_FONT` | SansSerif Plain 13pt | Subtitles, field labels |
| `TABLE_FONT` | SansSerif Plain 14pt | Table cells |

**Styling Methods:**
| Method | What it styles |
|--------|---------------|
| `applyGlobalTheme()` | Sets UIManager defaults for ALL Swing components. Called once at startup. |
| `styleButton(btn)` | Blue button with hover effect |
| `styleDangerButton(btn)` | Red button with hover effect |
| `styleSuccessButton(btn)` | Green button with hover effect |
| `styleSecondaryButton(btn)` | White button with grey border and hover |
| `styleTable(table)` | Alternating row colours, padding, nice headers |
| `styleTextField(field, label)` | Styled text input with titled border |
| `createCard(padding)` | White panel with shadow border |
| `createFilterPanel()` | Top filter bar styling |
| `createButtonPanel()` | Bottom action bar styling |

---

## 🗃️ Data Files (`.txt` Storage)

All data is stored in `data/db/`. Each file uses a **pipe-delimited (`|`)** format — one record per line. This replaces a traditional database as required by the assignment.

### `users.txt`
**Format:** `id|username|password|role|fullName|contact`

**Example:**
```
ADM1|admin|admin123|Administrator|Deependra Adhikari|9841234567
MAN2|manager|manager123|Manager|Rajesh Hamal|9851234568
SCH3|staff|staff123|Scheduler|Suman Shrestha|9861234569
CUST5|dipen|dipen123|Customer|Dipen Bhatta|9822611896
CUST6|dixita|dixita123|Customer|Dixita Sharma|9801234571
CUST7|anchita|anchita123|Customer|Anchita Gurung|9812345672
```

### `halls.txt`
**Format:** `id|name|type|capacity|ratePerHour|description`

**Example:**
```
HALL1|Everest Grand Auditorium|Auditorium|1000|300.0|Prestigious auditorium for grand conferences
HALL2|Annapurna Banquet Hall|Banquet Hall|300|100.0|Elegant banquet hall perfect for weddings
HALL3|Lumbini Meeting Room|Meeting Room|30|50.0|Cozy meeting room ideal for discussions
```

### `schedules.txt`
**Format:** `id|hallId|startTime|endTime|type|remarks`

**Example:**
```
SCH1|HALL1|2024-07-01T08:00:00|2024-08-31T18:00:00|AVAILABILITY|Open for bookings
SCH7|HALL1|2024-08-05T08:00:00|2024-08-05T14:00:00|MAINTENANCE|AC servicing
```

### `bookings.txt`
**Format:** `id|customerId|hallId|startTime|endTime|totalPrice|status|remarks`

**Example:**
```
BOOK1|CUST5|HALL1|2024-07-10T09:00:00|2024-07-10T13:00:00|1200.0|PAID|Nepal IT Conference 2024
BOOK14|CUST11|HALL5|2024-08-18T10:00:00|2024-08-18T13:00:00|300.0|CANCELLED|Photography Exhibition
```

### `issues.txt`
**Format:** `id|customerId|bookingId|description|status|assignedTo`

**Example:**
```
ISSUE1|CUST5|BOOK1|Sound system had echo issues during the conference|DONE|SCH3
ISSUE3|CUST8|BOOK4|Lighting was too dim in the banquet hall|OPEN|NONE
```

---

## 👤 User Roles & Features

### 🗓️ Scheduler
- Add, view (with search filter), edit, and delete hall records.
- Set **availability windows** (date + time range) so customers can book.
- Set **maintenance windows** (date + time range) to block booking during repairs.
- Write optional remarks for both types of schedules.
- View assigned maintenance issues.

### 👥 Customer
- Self-register and log in.
- Update profile (full name, contact number) — persisted to file.
- Browse halls available within scheduler-set AVAILABILITY windows.
- Book a hall (validated against: business hours 8 AM–6 PM, no overlap with other bookings or maintenance).
- Pay and receive a printed receipt popup.
- View upcoming and past bookings, with filters (All/Upcoming/Past/CANCELLED/PAID).
- Cancel a booking — **only if cancellation is ≥3 days before the event start date**.
- Raise an issue/complaint linked to a specific booking.

### 🛡️ Administrator
- Manage Scheduler staff accounts (add, view, edit, delete) with search filter.
- View all users (customers + staff) with search filter.
- Block or delete any user account.
- View all bookings across all customers (central view).

### 📊 Manager
- View a **sales dashboard** with totals for Current Week, Month, and Year.
- View all customer-raised issues with search filter.
- Assign a Scheduler to resolve an issue.
- Update issue status: `OPEN` → `IN_PROGRESS` → `DONE` → `CLOSED` (or `CANCELLED`).

---

## 🧱 OOP Concepts Demonstrated

### 1. Encapsulation
Every model class uses **private fields** with public **getters and setters**.
```java
private String username;
public String getUsername() { return username; }
public void setUsername(String username) { this.username = username; }
```

### 2. Inheritance
`User` is an **abstract base class**. All four roles extend it:
```
User (abstract)
├── Customer
├── Scheduler
├── Administrator
└── Manager
```
Similarly, `BaseDashboard` is an abstract panel that all dashboards extend.

### 3. Polymorphism
`AuthService.getAllUsers()` returns `List<User>`, but each object is the correct concrete type at runtime. `MainFrame.loginSuccess()` dispatches the correct dashboard using a role-based switch.

### 4. Abstraction
- `User` is `abstract` — cannot instantiate directly.
- `BaseDashboard.addSidebarButtons()` is an `abstract` method that each dashboard must implement.

### 5. Static Factory Methods (Serialization Pattern)
Every model has `fromString(String)` for deserialization and `toString()` for serialization.

### 6. Interface Implementation
All model classes implement `java.io.Serializable`.

### 7. Service Layer Pattern (Separation of Concerns)
```
UI Layer  →  Service Layer  →  DataStorage (util)  →  .txt files
```

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

- **Use Case Diagram**: Should show 4 actors (Customer, Scheduler, Administrator, Manager) with their respective use cases mapped to Table 2.
- **Class Diagram**: Should show the `User` hierarchy, `Hall`, `Booking`, `Schedule`, `Issue`, and the service/utility classes with their relationships.
- **OOP Justification**: Reference the seven OOP concepts documented above, with specific class and method names.
- **Appendix**: Include screenshots of all five `.txt` files from `data/db/`.

---

*Hall Symphony Inc. — Hall Booking Management System | OOP Group Assignment*
