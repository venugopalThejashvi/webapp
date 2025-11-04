# Spring Boot with PostgreSQL Project DemoAssign8

This is a Spring Boot application that uses a PostgreSQL database. Follow the steps below to set up and run the project locally.

---

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 21 or higher**
- **Maven** (for building the project)
- **PostgreSQL** (installed and running)
- **Git** (for cloning the repository)

---

## Steps to Set Up and Run the Project

### 1. Clone the Repository

Clone the project repository to your local machine

### 2. Set Up the .env File

The application requires a .env file to provide the database connection details. Follow these steps:

1. Create a .env file in the root directory of the project.
2. Add the following environment variables to the .env file:

DB_URL=jdbc:postgresql://localhost:5432/your_database_name
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password

#### Replace the placeholders with your actual database details:

1. your_database_name: The name of your PostgreSQL database.
2. your_database_username: The username for your PostgreSQL database.
3. your_database_password: The password for your PostgreSQL database.

### 3. Set Up the PostgreSQL Database

1. Ensure PostgreSQL is running on your machine.
2. Create a new database in PostgreSQL with the name you specified in DB_URL.
3. Grant the necessary permissions to the user specified in DB_USERNAME.

### 4. Navigate to the CloudApplication Class

- In your IDE, you need to navigate to the CloudApplication class.This class will be located in the src/main/java directory of the project.
- Run the main method in your ide

### 5. Access the Application
Once the application is running, you can check its health status by accessing the following endpoint in your browser or using a tool like curl or Postman:
curl -vvvv http://localhost:8080/healthz

### Troubleshooting

#### 1. Database Connection Issues: Ensure that PostgreSQL is running and the credentials in the .env file are correct.
#### 2. Port Conflicts: If port 8080 is already in use, update the server.port property in application.properties.
#### 3. Missing .env File: Ensure the .env file exists in the root directory and contains the required variables.


## Importing Namecheap SSL Certificate into AWS ACM
To import the Namecheap SSL certificate into AWS Certificate Manager for the <aws-profile> account, run:

```bash
aws acm import-certificate \
--certificate fileb://yourdomain.crt \
--private-key fileb://yourdomain.key \
--certificate-chain fileb://yourdomain.ca-bundle \
--region <aws-region> \
--profile <aws-profile>