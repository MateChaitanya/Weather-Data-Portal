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

## ⚙️ How It Works

1. User visits the portal and enters login credentials.  
2. Backend validates credentials against **LDAP directory**.  
3. On success → user is authenticated, and session is secured via **SSL/TLS**.  
4. User requests weather data (hourly/daily).  
5. Backend retrieves data from **MySQL database** and serves via APIs.  
6. Data is displayed on the frontend UI.  
7. All network requests flow securely: **Browser → Apache → Tomcat → Spring Boot**.  

---

## 📊 Example Use Case

If user searches **Pune**, the backend:  
- Looks up **city ID** from City Table.  
- Fetches **daily weather facts** for that city.  
- Retrieves **astronomy details** (sunrise, sunset).  
- Returns JSON response to frontend for display.  

---

## 🔑 Key Learning Outcomes

- Implementing **LDAP-based authentication** in enterprise applications.  
- Setting up **SSL/TLS certificates** for secure browser communication.  
- Configuring **Apache as reverse proxy** with Tomcat for production-grade deployment.  
- Structuring relational databases for weather-based applications.  
- Hands-on with **Spring Boot, Hibernate, and SQL queries**.

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

