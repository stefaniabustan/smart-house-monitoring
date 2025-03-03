# 🔌 Smart Energy Monitoring & Chat System

## 📌 Overview
This project is a **microservices-based system** designed for managing electrical devices, monitoring energy consumption, and enabling real-time communication between users. The system consists of an **admin dashboard** and a **client interface**, both implemented using **React** for the frontend and **Java Spring Boot** for the backend.

### 🎯 Features

#### 🔐 User Authentication
Users can log in securely using their credentials. The authentication system ensures only authorized users can access the platform.
![Login](/assets/login.PNG)

#### 👥 User Management
Admins can manage users by adding, editing, or deleting accounts. Each user is assigned a role (admin or user) and can be linked to specific devices.
![User Management](/assets/admin-UsersPage.PNG)


#### ➕ Adding a new user
Admins can add new users by providing a username, password, and selecting their user type.
![Add User](/assets/admin-AddUser.PNG)

#### 📊 Assigning Devices to Users
Admins can assign devices to users, allowing them to monitor and manage their respective energy consumption.
![Add device to user](/assets/admin-DevicesPage.PNG)

#### 📋 User Dashboard
Each user has a dashboard displaying their assigned devices and relevant energy data, improving user experience and accessibility.
![User's devices](/assets/Users-MainPage.PNG)

#### 💡 Device Management
Users can add and manage their smart devices, including providing descriptions, addresses, and monitoring energy consumption.
![Add device to user](/assets/admin-UsersPageAddDeviceToUser.PNG)

#### ⚡ Energy Consumption Monitoring
Users can select a date to view their energy consumption trends, visualized through graphs for better insights.
![Device consumption](/assets/Users-DeviceConsumption.PNG)



#### 🗨️ Real-time Chat
Users can communicate via real-time chat using WebSockets. This enables instant messaging between admins and users.
![Chat](/assets/Chat.PNG)







## 🏗️ Tech Stack
### Backend:
- **Java Spring Boot** (Microservices Architecture)
- **RabbitMQ** (Message Queue System)
- **Spring WebSockets** (Real-time Communication)
- **Spring Security** (Authentication & Authorization)
- **Spring Data JPA** (Database Management)
- **PostgreSQL** (Relational Database)

### Frontend:
- **React.js** (Admin and Client Dashboards)
- **Tailwind CSS** (UI Styling)

### Infrastructure:
- **Docker & Docker Compose** (Containerized Deployment)
- **NGINX Load Balancer** (Traffic Management)
- **Kubernetes (Optional)** (Orchestration for scaling)

## 📡 RabbitMQ Integration
RabbitMQ is used for inter-service communication.

**Message Flow Example:**
1. **Device data updates** are published to a queue.
2. **Notification service** listens for updates and processes alerts.
3. **Chat messages** are delivered asynchronously through RabbitMQ.

## 🔥 WebSockets for Real-time Communication
The chat system is powered by WebSockets, enabling instant messaging between users and admins.

### WebSocket Endpoints:
- **Connect**: `/ws/chat`
- **Send Message**: `/app/chat.sendMessage`
- **Subscribe to Messages**: `/topic/messages`

## ⚖️ Load Balancing
NGINX is configured as a load balancer to distribute requests across multiple backend instances, ensuring reliability and high availability.

```nginx
upstream backend {
    server backend-service-1:8080;
    server backend-service-2:8080;
}
server {
    location /api/ {
        proxy_pass http://backend;
    }
}
```

## 🏗️ Docker Deployment
The project is fully containerized with **Docker** and can be deployed with a single command:
```sh
docker-compose up -d
```

### Docker Services:
- `frontend`: React app
- `backend`: Spring Boot API
- `rabbitmq`: Message broker
- `postgres`: Database
- `nginx`: Load balancer
