# 🏛️ Hall Symphony — Hall Booking Management System

A high-performance, professional Java Desktop Application for streamlined hall reservations and management. Built with a focus on robust Object-Oriented Programming (OOP) principles and clean architectural separation.

---

## 🚀 Overview
**Hall Symphony Inc.** provides a comprehensive digital solution for managing event spaces efficiently. The system is designed to handle the complexity of scheduling, financial reporting, and customer relationship management within a single, unified interface.

### The Problem it Solves:
Traditional hall booking often suffers from double-bookings, fragmented communication, and lack of real-time availability. **Hall Symphony** eliminates these issues with a centralized scheduling engine and role-based access control.

---

## 👥 Roles & Responsibilities
The system is built around four primary user personas, each with a specialized dashboard and set of capabilities:

### 🛠️ Administrator (System Orchestrator)
The Administrator ensures the platform's integrity and manages the workforce.
- **Account Governance**: Full CRUD (Create, Read, Update, Delete) operations for all user accounts (Schedulers, Managers, Customers).
- **System Oversight**: Global view of all system activities and bookings to resolve high-level conflicts.
- **Security**: Ensures strict access control and data validation across the platform.

### 📅 Scheduler (Operations Lead)
The Scheduler is responsible for the physical inventory and timing of the halls.
- **Inventory Management**: Comprehensive Hall management, including adding new spaces and updating hall specifications.
- **Maintenance Planning**: Scheduling "Maintenance" slots to ensure halls are in peak condition, preventing bookings during these periods.
- **Availability Matrix**: Managing the master schedule to prevent overlaps and optimize hall utilization.

### 📈 Manager (Business Strategist)
The Manager focuses on growth, financial health, and service quality.
- **Business Intelligence**: Access to real-time sales reports (Weekly, Monthly, and Yearly) via the `ReportService`.
- **Revenue Analytics**: Tracking total revenue, booking trends, and hall performance metrics.
- **Quality Assurance**: Monitoring and addressing customer-reported issues to ensure high service standards.

### 👤 Customer (End User)
The Customer experiences the ease of booking through a streamlined self-service portal.
- **Self-Service Registration**: Easy onboarding process to create a personalized profile.
- **Discovery**: Browsing available halls with detailed descriptions and pricing.
- **Booking Lifecycle**: Creating reservations, making payments, and managing personal booking history.
- **Feedback Loop**: Reporting issues or providing feedback directly through the application.

---

## 🛠️ Core Technologies
- **Language**: Java 14+ (Leveraging modern features like Records and Streams)
- **Framework**: Java Swing (Custom GUI with high-fidelity rendering)
- **Persistence**: **SymphonyDB** — A custom flat-file (`.txt`) database system ensuring extreme portability without external SQL dependencies.
- **Styling**: Segoe UI typography with HD Anti-aliasing and a unified color tokens system.

---

## 🏗️ Project Architecture
The system follows a strict **N-Tier Architecture** for scalability and maintainability:

- **`com.hallsymphony.model`**: The domain layer containing POJOs (Plain Old Java Objects) like `Booking`, `Hall`, and `User`.
- **`com.hallsymphony.service`**: The business logic engine handling complex validations, financial calculations, and state transitions.
- **`com.hallsymphony.ui`**: A modular View layer built with custom Swing components and a fluid navigation system.
- **`com.hallsymphony.util`**: Cross-cutting concerns including Data I/O, regex-based validation, and UI styling utilities.

---

## 💎 OOP Concepts Applied
- **Encapsulation**: Rigid data protection using private fields and controlled accessors.
- **Inheritance**: A hierarchical `User` model (`Customer` ⮜ `User`, etc.) promotes code reuse.
- **Abstraction**: Base controllers and dashboards define mandatory behaviors for all modules.
- **Polymorphism**: Dynamic service loading allows the UI to adapt seamlessly based on the logged-in user's role.

---

## ▶️ Setup & Execution

### Prerequisites
- **JDK 14+** installed.

### Quick Start
1.  **Compile**:
    ```bash
    javac -sourcepath src -d bin HallSymphony.java
    ```
2.  **Execute**:
    ```bash
    java -cp bin HallSymphony
    ```

---

## 📂 Project Structure
```text
HallSymphony/
├── src/                # Pure source code (Clean Separation)
│   └── com/hallsymphony/
│       ├── model/      # Entity definitions
│       ├── service/    # Business rules
│       ├── ui/         # Visual components
│       └── util/       # Foundation utilities
├── data/db/            # SymphonyDB (Flat-file Storage)
├── bin/                # Compiled bytecode
└── HallSymphony.java   # Main entry point
```
---
