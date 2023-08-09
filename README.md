# Human Creator: Getting Started
This guide will walk you through running a Human Creator application. Make sure you have Java 8 with integrated JavaFX installed on your machine. You can use a distribution like Corretto 1.8.

# Prerequisites
1. **Java 8 with integrated JavaFX:** Ensure that you have Java 8 (e.g., Corretto 1.8.0_382) installed on your machine.

2. **Docker:** You need Docker installed to run the MySQL Docker container.

# Running the Application
1. **Clone the Repository:** Clone the repository to your local machine.

2. **Navigate to the Repository:** Open a terminal and navigate to the repository's directory.

3. **Execute run.sh:** Run the run.sh script in the terminal. This script does the following:

   * Starts a MySQL Docker container with the specified configuration.
   * Runs the JavaFX application.
   
     ```./run.sh```

   * Enjoy the Human Creator Application: The JavaFX application should launch and be ready for use.

# Cleaning Up
After you're done with the application, you can stop the Docker containers using the following command:

```docker-compose down```

