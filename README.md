
# Restaurant Project Setup

## Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

---

## Setup Instructions

### 1. Create MySQL Database
Run the following command to create a database:
```sql
CREATE DATABASE restaurant;
```

### 2. Configure Spring Boot DataSource
Update the Spring Boot configuration file (`src/main/resources/application.properties`) with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/restaurant
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```
Replace `<your-username>` and `<your-password>` with your MySQL credentials.

---

### 3. Update JWT Secret
Set the JWT secret in `application.properties`:
```properties
jwt.secret=<your-jwt-secret>
```
Replace `<your-jwt-secret>` with your desired secret value.

---

### 4. Create Image Upload Directory
Create a folder in your system to store uploaded images. Use the following path:
```
<project-directory>/upload/product/avatar
```

### 5. Update File Path in Application
In `application.properties`, add the absolute path to the folder created above:
```properties
file.path=<absolute-path-to-upload-folder>
```
Replace `<absolute-path-to-upload-folder>` with the full path to the `upload/product/avatar` folder. For example:
```properties
file.path=/Users/yourname/project/upload/product/avatar
```

### 6. Update origin url (if needed)
```properties
origins.url=http://localhost:3001
```

---

### 7. Run the Application
Use Maven to build and run the application:
```bash
mvn spring-boot:run
```

---

### Notes
- Ensure that the upload folder has the correct read and write permissions.
- The `spring.jpa.hibernate.ddl-auto=update` will automatically create/update tables in the database, but it's recommended to switch to `validate` in production to avoid accidental schema changes.
