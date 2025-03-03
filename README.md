# 🔌 Smart Energy Monitoring & Chat System

## 📌 Overview
This project is a **microservices-based system** designed for managing electrical devices, monitoring energy consumption, and enabling real-time communication between users. The system consists of an **admin dashboard** and a **client interface**, both implemented using **React** for the frontend and **Java Spring Boot** for the backend.

### 🎯 Features
- **User Management**: Admins can add, edit, or remove users and assign them devices.
- **Device Management**: Users can register, monitor, and analyze energy consumption of their smart devices.
- **Real-time Chat**: Users and admins can communicate via a chat system powered by WebSockets.
- **Load Balancing**: Ensures optimal traffic distribution for system stability and scalability.
- **RabbitMQ Messaging**: Facilitates inter-service communication for event-driven interactions.
- **Docker Deployment**: Containerized services for easy deployment and scalability.

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

## ⚡ System Architecture
```
+----------------------+       +----------------------+
|  React Frontend (UI) | <---> |  Spring Boot Backend |
+----------------------+       +----------------------+
         |                                |
         v                                v
+----------------+    +-----------------+    +----------------+
| WebSockets    |<-->|  RabbitMQ        |<-->| PostgreSQL DB  |
+----------------+    +-----------------+    +----------------+
```

## 🚀 Getting Started

### Prerequisites
Ensure you have the following installed:
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Node.js](https://nodejs.org/en/download/)
- [Java 17+](https://adoptium.net/)
- [PostgreSQL](https://www.postgresql.org/download/)

### Installation
#### 1️⃣ Clone the Repository:
```sh
 git clone https://github.com/your-repository.git
 cd your-repository
```

#### 2️⃣ Backend Setup
```sh
cd backend
./mvnw clean install
```

#### 3️⃣ Frontend Setup
```sh
cd frontend
npm install
```

#### 4️⃣ Start Services using Docker
```sh
docker-compose up --build
```

#### 5️⃣ Access the Application
- **Admin Dashboard**: `http://localhost:3000/admin`
- **Client Dashboard**: `http://localhost:3000/client`

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

## 📜 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Contributors
- **Your Name** - [GitHub Profile](https://github.com/your-profile)

---
🎯 *Feel free to fork and contribute!* 🚀

