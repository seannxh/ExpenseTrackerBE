# 💸 ExpenseTracker API

A secure, production-ready **expense tracking REST API** built with **Java Spring Boot**, integrated with **Amazon RDS (MySQL)**, and deployed to **AWS Elastic Beanstalk**.  
It includes robust user authentication (JWT + OAuth2), personalized expense tracking, and thorough testing with JUnit and Mockito.

---

## 🌐 Live Demo

**Base URL**  
[[http://expensetracker-env.eba-xxxxxxxx.us-east-2.elasticbeanstalk.com](http://expensetracker-env.eba-xxxxxxxx.us-east-2.elasticbeanstalk.com)](http://expensetracker-env.eba-2mpjph9f.us-east-2.elasticbeanstalk.com/)

**Swagger UI (if enabled)**  
[http://expensetracker-env.eba-xxxxxxxx.us-east-2.elasticbeanstalk.com/swagger-ui/index.html](http://expensetracker-env.eba-xxxxxxxx.us-east-2.elasticbeanstalk.com/swagger-ui/index.html)

> Use Postman or connect your frontend client to interact with the API endpoints.

---

## ⚙️ Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Security** (JWT + OAuth2)
- **Amazon RDS** (MySQL)
- **AWS Elastic Beanstalk**
- **Spring Data JPA**
- **Maven**
- **JUnit & Mockito**
- **Postman**
- **Swagger (OpenAPI 3)**

---

## ✅ Key Features

- **Authentication**
  - Secure signup and login with JWT
  - Passwords encrypted with BCrypt
  - OAuth2 login (e.g. Google) supported
- **Expense Management**
  - CRUD operations for expenses
  - User-specific data access
- **Testing**
  - Unit testing with JUnit
  - Mocking with Mockito
- **Deployment**
  - Packaged as `.jar` and deployed to AWS Elastic Beanstalk
  - Connected to Amazon RDS (MySQL)
  - Configured via environment variables

---

## 📬 API Endpoints

**Base path: `/api`**

| Method | Endpoint            | Description                     |
|--------|---------------------|---------------------------------|
| POST   | `/auth/signup`      | Register a new user             |
| POST   | `/auth/login`       | Authenticate and get JWT        |
| GET    | `/expenses`         | Retrieve all user expenses      |
| POST   | `/expenses`         | Create a new expense            |
| PUT    | `/expenses/{id}`    | Update an expense by ID         |
| DELETE | `/expenses/{id}`    | Delete an expense by ID         |

> Endpoints under `/expenses` require `Authorization: Bearer <JWT>` in headers.

---

## 🗂️ Project Structure
src/
├── config/ # Spring Security configuration
├── controller/ # REST API endpoints
├── model/ # Entity classes
├── repository/ # JPA repositories
├── service/ # Business logic
├── security/ # JWT filter and utility classes
├── dto/ # Data transfer objects
└── test/ # JUnit and Mockito test cases

w
---

## 🛠️ Running the App Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/expensetracker-api.git
   cd expensetracker-api

2. Set up your local MySQL database or connect to RDS.

3. Configure your application.properties or use environment variables.
4. Build and run:

bash
Copy
Edit
./mvnw clean package
java -jar target/expensetracker-0.0.1-SNAPSHOT.jar

🔐 Environment Variables
Set these in your local environment or through AWS Elastic Beanstalk → Configuration → Software → Environment Properties:
SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/expensetracker
SPRING_DATASOURCE_USERNAME=your-db-username
SPRING_DATASOURCE_PASSWORD=your-db-password
jwt.secret=your-jwt-secret-key
spring.security.oauth2.client.registration.google.client-id=your-google-client-id
spring.security.oauth2.client.registration.google.client-secret=your-google-client-secret


