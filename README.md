# STAN_CHATBOT_PROJECT

Welcome to the **STAN_CHATBOT_PROJECT**! This project is designed to create an intelligent chatbot application that can interact with users and provide responses based on predefined logic and data.

## Project Overview

The STAN_CHATBOT_PROJECT is a **Java-based chatbot application** that utilizes Natural Language Processing (NLP) techniques to understand and respond to user inputs. The project is structured to be modular and scalable, allowing for easy integration of additional features and improvements.

## Technologies Used

- **Java**: Core programming language for backend development.
- **Spring Boot**: Framework for building backend services.
- **Maven**: Dependency management and build automation tool.
- **HTML/CSS**: Frontend structure and styling.
- **JavaScript**: Interactive elements on the frontend.
- **Thymeleaf**: Templating engine for rendering HTML views.
- **Application Properties**: Configuration settings.

## Setting Up the Project

### Step 1: Clone the Repository

```bash
git clone https://github.com/kmanasagithub/STAN_CHATBOT_PROJECT.git
cd STAN_CHATBOT_PROJECT
```

### Step 2: Configure Application Properties

Edit the application.properties file located in src/main/resources:

## Configuration: Setting Up `application.properties`

Before running the project, you need to configure some environment-specific variables in the `application.properties` file located at `src/main/resources/application.properties`.  

Here are the variables you need to set:

```properties
# Spring Boot application name and server port
spring.application.name=stan-chatbot-java
server.port=8080

# MySQL connection details
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/${DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# Hibernate / JPA settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

# Spring Boot settings
spring.main.banner-mode=off
logging.level.root=warn

# Gemini API Key (for chatbot integration)
gemini.api.key=${GEMINI_OPEN_API_KEY}
```
### Instructions

### Database Configuration
- Replace `${DB_NAME}` with the name of your MySQL database.  
- Replace `${DB_USERNAME}` and `${DB_PASSWORD}` with your MySQL credentials.  

### Gemini API Key
- Replace `${GEMINI_OPEN_API_KEY}` with your OpenAI/Gemini API key to enable AI-powered chatbot responses.  

### Optional
- You can change `server.port` if you want the application to run on a different port.  
- Modify `spring.application.name` if you want a different application name.  


### Step 3: Build the Project

```bash
mvn clean install
```
This command compiles the source code, runs tests, and packages the application.

### Step 4: Run the Application

```bash
mvn spring-boot:run
```
Or run the application from your IDE if it supports Spring Boot.

### Step 5: Accessing the Application

Once the project is running, you can open the chatbot in your web browser at the port specified in your `application.properties` file.  
For example, if you set

```properties
server.port=8080
```
Then open your browser and go to:

```bash
http://localhost:8080
```
You should see the chatbot interface and can start interacting with it.

## Project Structure

src/main/java
├── com.stan.chatbot # Main package containing all backend logic
│ ├── controller # Handles HTTP requests and responses
│ ├── service # Business logic for chatbot interactions
│ └── model # Data structures used in the application

src/main/resources
├── application.properties # Configuration file
├── templates # HTML templates for rendering views
└── static # Static resources like CSS and JavaScript files
