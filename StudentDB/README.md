# Student-Management-System-Java
A modular Java + MySQL backend system with validation, GUI support, CSV import, and faculty-student relationship features.
# Student Management System (Java + MySQL)

A **Student Management System** built with **Java** and **MySQL** that allows you to manage student and faculty records, update details, import data from CSV files, and more. The project utilizes an interactive **Java GUI** built using Swing components for user-friendly experience.

## Features

- **Student Management:**
  - Add, update, delete, and view student details.
  - Validate input for various fields such as name, age, email, etc.
  
- **Faculty Management:**
  - Add, update, and view faculty details.
  - Import faculty data from CSV files.

- **CSV Data Import:**
  - Import student and faculty data directly from CSV files.

- **Database Integration:**
  - Uses **MySQL** database for storing student and faculty information.
  - Supports dynamic updates to the database via Java methods.

- **Input Validation:**
  - Ensures correct format for fields like name, email, and age.
  
## Technologies Used

- **Java** (Swing for GUI, JDBC for database interaction)
- **MySQL** (Database)
- **CSV Files** (Data Import)
- **Git** (Version control)

## Requirements

- Java 8 or higher
- MySQL
- A database named `student_management_system` with the following tables:
  - `students` (id, name, age, email, course, status)
  - `faculty` (id, name, email, department, facultyID)

## Getting Started

1. **Clone the repository**:

   ```bash
   git clone https://github.com/Inala-Venkata-Hari/Student-Management-System-Java.git
2. Set up the database:

Ensure MySQL is installed.

Create a database named student_management_system:


CREATE DATABASE student_management_system;
Import the provided SQL schema to create the necessary tables.

3. Run the application:

Open the project in your preferred Java IDE (e.g., IntelliJ IDEA, Eclipse).

Run the Main.java file to start the GUI.

4. Usage:

Add, update, and delete student and faculty records via the GUI.

Import data from CSV files to add records.

Validate input as you interact with the system.
