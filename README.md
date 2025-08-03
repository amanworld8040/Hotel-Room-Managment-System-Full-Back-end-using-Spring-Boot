# Hotel Room Booking System (Back-end, Java & Spring Boot)

This is a Back-end hotel room booking system developed with Java and Spring Boot. The application runs as a console-based service, allowing users to manage hotel rooms and bookings.

## 🌟 Features
- **Room Management:** Add, view, update, and delete hotel rooms.
- **Booking System:** Create and manage room reservations for guests.
- **Availability Check:** Check room availability for specific dates.
- **Guest Management:** Maintain a list of guests and their booking history.
- **Console Interface:** All interactions are performed via a command-line interface.

## 🛠️ Prerequisites
Before you begin, ensure you have the following installed on your system:
- **Java Development Kit (JDK) 17 or higher:** The application is built with modern Java features.
- **Apache Maven:** Used for project building and dependency management.
- **Git:** For cloning the repository.
- **A text editor or IDE:** Such as IntelliJ IDEA, VS Code, or Eclipse.

## 🚀 Getting Started
Follow these steps to get a copy of the project up and running on your local machine.

### 1. Clone the Repository
Open your terminal or command prompt and clone the project to your local directory:
```bash
git clone https://github.com/amanworld8040/Hotel-Room-Managment-System-Full-Back-end-using-Spring-Boot.git
```

### 2. Navigate to the Project Directory
Change your current directory to the cloned project folder:
```bash
cd Hotel-Room-Managment-System-Full-Back-end-using-Spring-Boot
```

### 3. Build the Project with Maven
Use Maven to compile the project and package it into an executable JAR file.
```bash
./mvnw clean install
```
This command will download all necessary dependencies and build the project. The final JAR file will be located in the `target/` directory.

### 4. Run the Application
You can run the application from the command line using the generated JAR file:
```bash
java -jar target/Hotel-Room-Managment-System-Full-Back-end-using-Spring-Boot-0.0.1-SNAPSHOT.jar
```
Alternatively, if you are using an IDE like IntelliJ IDEA or Eclipse, you can simply run the main application class (e.g., `com.example.yourproject.YourApplication.java`).

The application will start, and you will see a console-based menu to interact with the system.

## 💻 Technologies Used
- **Java:** The primary programming language.
- **Spring Boot:** Framework for building the application, providing configuration and dependency injection.
- **Maven:** Project management and build automation tool.
- **Spring Data JPA:** For database interaction and object-relational mapping.
- **PostgreSql Database:** An in-memory database used for development and testing.

## 🤝 Contributing
Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are greatly appreciated.

1. **Fork** the Project
2. **Create your Feature Branch** (`git checkout -b feature/AmazingFeature`)
3. **Commit your Changes** (`git commit -m 'Add some AmazingFeature'`)
4. **Push to the Branch** (`git push origin feature/AmazingFeature`)
5. **Open a Pull Request** create once again
