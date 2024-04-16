
# Cars Database Application

## Overview
This JavaFX application is designed to manage a car database. It provides functionality for searching, inserting, deleting, and updating records within the database. 

## Prerequisites
Before running this application, ensure you have the following installed:
- JDK 11 or higher
- MySQL Server (any recent version should work)
- MySQL Connector/J (ensure it's included in your project's library path)

## Setup Instructions

### 1. Clone the Repository
Clone the repository to your local machine. You can do this from a command line interface with the following command:
```bash
git clone [URL_TO_REPOSITORY]
```
Replace `[URL_TO_REPOSITORY]` with the actual URL to the repository.

### 2. Database Setup
Before running the application, you need to set up the database schema and initial data:
- Start your MySQL server.
- Log in to your MySQL database.
- Run the SQL script `cars.sql` to create the database schema and populate it with initial data. You can do this through your MySQL client interface or from the command line:
```bash
mysql -u [username] -p [database_name] < path/to/cars.sql
```
Replace `[username]`, `[database_name]`, and `path/to/cars.sql` with your MySQL username, the name of your database, and the path to the `cars.sql` file, respectively.

### 3. Configure Database Connection
Open the project in your Java IDE and navigate to the `DataBaseGUI.java` file. Update the database connection settings to match your MySQL server configuration:
```java
c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars", "root", "");
```
Replace `"root"` and the empty string with your database username and password.

### 4. Configure IDE
After cloning the repository, navigate to the `.vscode` folder and open the `launch.json` file. Copy all of the `vmArgs` from the second configuration and paste them into the first configuration. This will ensure that the application has all necessary JVM arguments set when launched.

### 5. Run the Application
Now, you can run the `DataBaseGUI.java` file in your IDE to start the application. Ensure that your database server is running before you launch the application.

## Troubleshooting
If you encounter connection issues, check your database username and password. Make sure your MySQL server is running and that the `cars` database exists and is populated correctly. If errors persist, check the MySQL server logs for more details.

## License
This project is licensed under the MIT License - see the LICENSE.md file for details.

## Author
This application was developed by Yousef Albandak.
