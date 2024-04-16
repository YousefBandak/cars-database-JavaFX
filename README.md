
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

### VSCODE Setup Instructions

After cloning the repository, navigate to the `.vscode` folder and open the `launch.json` file. Copy all of the `vmArgs` from where it's present in the configurations and paste where it's not present in any configurations.

### IntelliJ IDEA Setup Instructions

#### Step 1: Import the Project
- Open IntelliJ IDEA.
- Select 'Import Project' and choose the project directory where you cloned the repository.
- IntelliJ will detect the existing sources. Choose 'Import project from external model' and select 'Gradle', 'Maven', or just 'Java' if it's a simple Java project.

#### Step 2: Configure the JDK
- Go to 'File > Project Structure' (Ctrl+Alt+Shift+S).
- Under 'Project Settings', select 'Project'.
- From the 'Project SDK' dropdown, select your JDK version (JDK 11 or higher). If it’s not listed, click 'New' and specify the path to the JDK.

#### Step 3: Add JavaFX SDK as a Library
- Still in the 'Project Structure', go to 'Libraries'.
- Click the '+' button and select 'Java'.
- Navigate to the JavaFX SDK 'lib' folder on your system and select it.
- Click 'OK' to add it as a library and then 'Apply'.

#### Step 4: Configure Run/Debug Configuration
- Go to 'Run > Edit Configurations'.
- Click the '+' to add a new configuration and select 'Application'.
- In the 'Name' field, give a name to your configuration, like 'DatabaseGUI'.
- In the 'Main Class' field, specify the main class (e.g., 'DataBaseGUI').
- Click on 'Modify options' and select 'Add VM options'.
- In the 'VM options' field, input:
  ```
  --module-path .vscode\\javafx-sdk-18.0.1\\lib --add-modules javafx.controls,javafx.fxml
  ```
- Click 'OK' to save the configuration.

#### Step 5: Run the Application
- Select the run configuration you just created from the dropdown menu in the toolbar.
- Click the 'Run' button (Shift + F10) to start the application.

### 5. Run the Application
Now, you can run the `DataBaseGUI.java` file in your IDE to start the application. Ensure that your database server is running before you launch the application.

## Troubleshooting
If you encounter connection issues, check your database username and password. Make sure your MySQL server is running and that the `cars` database exists and is populated correctly. If errors persist, check the MySQL server logs for more details.

## License
This project is licensed under the MIT License - see the LICENSE.md file for details.

## Author
This application was developed by Yousef Albandak.
