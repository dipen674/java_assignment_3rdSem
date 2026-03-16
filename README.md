# 🏛️ Hall Symphony — Hall Booking Management System

A high-performance, professional Java Desktop Application for streamlined hall reservations and management. Built with a focus on robust OOP principles and clean architectural separation.

---

## 🚀 Overview
**Hall Symphony Inc.** provides a comprehensive digital solution for managing event spaces. The system supports multi-role access (Admin, Manager, Scheduler, Customer) and ensures data integrity through a custom flat-file database system.

### Key Capabilities:
- **Customers**: Self-registration, hall browsing, real-time booking, and issue reporting.
- **Schedulers**: Comprehensive hall CRUD and availability/maintenance scheduling.
- **Administrators**: Centralized user account control and global booking oversight.
- **Managers**: Strategic business analytics and service quality management.

---

## 🛠️ Core Technologies
- **Language**: Java 14+
- **Framework**: Java Swing (GUI)
- **Persistence**: Flat-file (`.txt`) database system
- **Styling**: Custom Design System (Segoe UI, HD Anti-aliasing)

---

## 🏗️ Project Architecture
The system follows a strict **N-Tier Architecture** for scalability and maintainability:

- **`com.hallsymphony.model`**: Entity classes (POJOs) representing core data structures.
- **`com.hallsymphony.service`**: Business logic layer handling validations, calculations, and data processing.
- **`com.hallsymphony.ui`**: Graphical layer built with Swing and custom design tokens.
- **`com.hallsymphony.util`**: Shared utilities for I/O, validation, and styling.

---

## 💎 OOP Concepts Applied
This project serves as a showcase for advanced Object-Oriented Programming:

1.  **Encapsulation**: All entity fields are `private`, with access restricted via controlled getters/setters in the `model` package.
2.  **Inheritance**: A hierarchical `User` model allows `Customer`, `Administrator`, `Scheduler`, and `Manager` to share base identity attributes while extending specific behaviors.
3.  **Abstraction**: Use of `abstract` classes for `User` and `BaseDashboard` defines rigid contracts for specialized implementations.
4.  **Polymorphism**: Dynamic dispatch is used extensively in `AuthService` and `MainFrame` to handle different user types through common interfaces.

---

## ▶️ Setup & Execution

### Prerequisites
- **JDK 14+** installed and configured in your PATH.

### Installation & Run
1.  **Compile**: From the project root, run:
    ```bash
    javac -sourcepath src -d bin HallSymphony.java
    ```
2.  **Execute**: Run the compiled application:
    ```bash
    java -cp bin HallSymphony
    ```

---

## 📂 Project Structure
```text
HallSymphony/
├── src/                # Source code
│   └── com/hallsymphony/
│       ├── model/      # Data entities
│       ├── service/    # Business logic
│       ├── ui/         # User interface
│       └── util/       # Infrastructure
├── data/db/            # Persistent storage (.txt)
├── bin/                # Compiled artifacts
└── HallSymphony.java   # App entry point
```

---
*Professional Grade | Scalable Architecture | Academic Excellence*
