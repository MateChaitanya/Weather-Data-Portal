# 🌦️ Weather Data Portal

A full-stack **Weather Data Portal** built with **Spring Boot, Java, SQL, LDAP, SSL/TLS, Apache (Reverse Proxy), and Tomcat**, providing secure user authentication and weather data management.  

This project demonstrates **backend system security, authentication with LDAP, API integration, and frontend–backend communication**.

---

## 🚀 Features

### 🔐 Authentication & Security
- **LDAP-based authentication**: Users must log in with credentials stored in LDAP.
- **Password security**: Passwords are securely handled and validated against LDAP.
- **SSL/TLS enabled**: All browser–server communication is encrypted, preventing data leaks.

### 🗄️ Backend Development
- **Spring Boot REST APIs** for user authentication and weather data handling.
- **MySQL Database** for storing historical weather data.
- **Hibernate ORM** used for efficient database interaction.
- **Data Queries**: Fetch city-wise weather details, hourly/daily facts, and astronomy details.

### ☁️ Weather Data Features
- **City Table**: Stores city metadata (name, coordinates, etc.).
- **Daily Facts Table**: Stores day-wise weather data (temperature, humidity, etc.).
- **Astronomy Table**: Stores sunrise, sunset, moonrise, and moonset data.
- **Relational Mapping**: Tables connected via foreign keys for structured queries.

### ⚡ Infrastructure & Deployment
- **Apache HTTP Server** configured as a **reverse proxy** to manage incoming traffic.
- **Tomcat Server** hosts the Spring Boot backend application.
- All requests are routed through Apache → Tomcat → Application.

### 🖥️ Frontend Integration
- Simple **frontend UI** (can be extended) for login and weather data visualization.
- Fetches data securely from backend APIs after user authentication.

---

## 🛠️ Tech Stack

- **Languages:** Java, SQL  
- **Backend Frameworks:** Spring Boot, Hibernate  
- **Authentication:** LDAP (Lightweight Directory Access Protocol)  
- **Security:** SSL/TLS  
- **Database:** MySQL  
- **Web Servers:** Apache (Reverse Proxy), Tomcat  
- **Tools:** Postman, IntelliJ IDEA, GitHub  

---

## 📂 Project Structure

```bash
Weather-Data-Portal/
│
├── backend/                 # Spring Boot backend
│   ├── src/main/java/       # Java source code
│   ├── src/main/resources/  # Application config files
│   └── pom.xml              # Maven dependencies
│
├── frontend/                # Frontend code (UI for login + weather view)
│
├── README.md                # Project documentation
