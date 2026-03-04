# Human Resource Management System

A console-based **Java** application for managing employees, attendance, salary calculation, and HR reports.
The system follows a layered architecture (UI → Manager → DAO → File Storage) and uses file-based persistence.

## Description

**The Human Resource Management System (HRMS)** is designed to manage employee records, track attendance, calculate monthly salaries, and generate reports.

This project demonstrates:

- Object-Oriented Programming (OOP)

- Layered architecture

- File handling using Java NIO

- Custom exception handling

- Input validation

-   Clean separation of responsibilities

## Features

### Employee Management

- Add new employees (Full-time / Part-time)

- Update employee information

- Remove employee

- Activate / Deactivate employee

- Search by:

    - Name

    - Department

    - Job Title

- View all employees

### Attendance Management

- Record daily attendance

- Update attendance record

- View attendance history

- View monthly attendance summary:

    - Working days

    - Absent days

    - Overtime hours

### Salary Management

- Calculate monthly salary

- View salary detail

- Generate monthly salary report

- Automatic:

    - Overtime calculation

    - Absence deduction

    - Total salary computation

### Reports

- Employees with low attendance (absent > threshold)

- Top N highest paid employees in a month

## Learning Objectives

This project demonstrates:

- Clean code organization

- SOLID-style separation of concerns

- Custom exception handling

- Predicate-based flexible search

- File-based persistence instead of database

- Single-pass data aggregation optimization