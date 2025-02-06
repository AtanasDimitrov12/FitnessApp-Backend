
# **📌 README for Backend (`FitnessApp-Backend`)**
```markdown
# 🏋️‍♂️ Fitness App - Backend (Dockerized)

This repository contains the **backend** of the Fitness App, built with **Spring Boot**, designed to be **fully Dockerized**. The **easiest way** to run this backend is using **Docker Compose**, which will start both the backend **and the database**.

## 🚀 Features
- **User Authentication** – JWT-based login for users & admins.
- **Workout & Progress Tracking** – Log workouts and track fitness goals.
- **Meal Planning** – Manage meal recommendations and diets.
- **WebSockets for Real-Time Notifications** – Instant updates when workouts are completed.
- **Database Management** – MySQL database with JPA persistence.

## 📂 Tech Stack
- **Backend Framework**: Java, Spring Boot
- **Database**: MySQL
- **ORM**: JPA (Hibernate)
- **Authentication**: JWT (JSON Web Tokens)
- **WebSockets**: Spring WebSockets
- **Containerization**: Docker, Docker Compose
- **Build Tool**: Gradle

---

## **🐳 Running the Backend with Docker (Recommended)**
This backend is designed to run **inside Docker** using `docker-compose`.

### **1️⃣ Clone the Repository**
```sh
git clone https://github.com/AtanasDimitrov12/FitnessApp-Backend.git
cd FitnessApp-Backend
