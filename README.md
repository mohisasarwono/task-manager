# 📋 Task Manager API

A simple REST API for personal task management, built as a portfolio project to learn and practice end-to-end backend development with Spring Boot — from database design and authentication to API security best practices.

## 🎯 About This Project

This project is part of my learning journey toward a **Junior/Entry-Level Backend Developer** role, with a focus on applying core Spring Boot concepts in a near real-world scenario:

- JWT-based authentication & authorization (access token + refresh token)
- Entity relationships and database queries
- Input validation and centralized error handling
- Consistent layer separation (Controller–Service–Repository)

## 🛠️ Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 4.0.8
- **Database:** MySQL + Spring Data JPA (Hibernate)
- **Authentication:** Spring Security + JWT (JJWT)
- **Build Tool:** Maven
- **Object Mapping:** ModelMapper

## ✨ Features

- Register & Login with JWT
- Task CRUD (Create, Read, Update, Delete)
- Filter tasks by status
- Each user can only access their own tasks
- Input validation & centralized error handling (`@RestControllerAdvice`)

## 🚀 Getting Started

### Prerequisites

- Java 17
- Maven
- MySQL

### Setup

1. Clone the repository
   ```bash
   git clone https://github.com/your-username/taskmanager.git
   cd taskmanager
   ```

2. Create a MySQL database
   ```sql
   CREATE DATABASE taskmanager_db;
   ```

3. Configure `src/main/resources/application.properties`
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/taskmanager_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   jwt.secretKey=your_secret_key
   ```

4. Run the application
   ```bash
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8080`.

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|--------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive a JWT |
| GET | `/api/tasks` | Get all tasks for the logged-in user |
| GET | `/api/tasks?status=DONE` | Filter tasks by status |
| GET | `/api/tasks/{id}` | Get a task by ID |
| POST | `/api/tasks` | Create a new task |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |

## 🤝 Development Process

This project was developed independently with the help of **Claude (Anthropic AI)** as a pair-programming partner and learning mentor — covering project structure planning, debugging runtime errors and Spring Security configuration, and discussions on clean code and design patterns. This approach was chosen to accelerate the learning process while understanding the *root cause* of each issue that came up, rather than just copy-pasting solutions.

## 📌 Status

🚧 Actively in development — additional features (pagination, unit tests, Dockerfile) are planned as next steps.
