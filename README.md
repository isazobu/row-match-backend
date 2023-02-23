

# Important Note

### While working on this case study, I had no prior experience with Java Spring Boot and Hibernate, but I was able to learn them from scratch in a short amount of time. As a result, I may not have adhered to Java Spring Boot best practices as well as I could have. However, I did my best to write code in a way that is as clean and efficient as possible, given my current level of expertise.

### Due to time constraints, I had to balance learning new technologies with delivering results. Therefore, there may be areas where I could have improved the code further if I had more time. Nonetheless, I am proud of what I was able to achieve with my existing knowledge and skills. I am always eager to learn and grow as a developer, and I am confident that I can quickly adapt to new technologies and best practices.

### Thank you for considering my case-study. I am excited about the opportunity to contribute my skills and experience to Dream Games.


# Potential Improvements for the Project

* Improve test cases and add more test scenarios for better coverage
* Implement   DTO (Data Transfer Object) to Entity mapping
* Implement reverse mapping from DTO to Entity
* Improve Swagger Documentation
* Implement logging to improve troubleshooting and debugging
* Git semantic messages and small, frequent commits could have been used to improve the project's maintainability and collaboration.
* Separate environment files for different deployment stages (e.g., development, staging, production)
* Dockerize the application to ensure consistency across different environments and simplify deployment
* Introduce caching with Redis to improve performance and reduce database load
* Add monitoring tools to track application performance and identify issues early
* Deploy the application to a cloud provider (e.g., AWS, GCP, Azure) and use load balancing to scale the application horizontally
* Implement automated deployment pipelines using CI/CD tools like Jenkins or CircleCI
* Consider implementing security measures like SSL/TLS encryption and OWASP Top 10 vulnerabilities testing
* Introduce performance optimization techniques like code profiling and optimization to ensure optimal performance and reduce resource usage


# Tables
User
---------------------------------
- id: Long
- name: String
- level: Integer
- coins: Integer
- token: String
- team: Team

 Team
---------------------------------
- id: Long
- name: String
- users: List<User>

# Use Case Scenarios

## Title: Create User
### User Story:

As a new user of the application, I want to be able to create an account and start playing, so that I can track my progress and compete with other players.
### Goal:
To create a new user with default values and return their unique ID, level, and coins.

### Actors:
User: the person or entity creating the new user.
System: the application or software creating and storing the user information.

### Preconditions:

The User has access to the System and has logged in as a valid user with the appropriate permissions to create a new user.

The System is running and connected to the necessary databases or services.

### Steps:

* The User initiates the "Create User" process by selecting the appropriate option or page in the System.

* The System presents the User with a form or fields to fill out for the new User's name and any other relevant information.
* The User fills out the required fields and submits the form or request.
* The System verifies that the User's name is unique and not already in use by another User.
* If the name is unique, the System generates a unique ID for the new User, sets their starting level to 1 and starting coins to 5,000.
* The System creates a new User object with the provided information and default values, and saves it to the database.
* The System returns the new User's unique ID, level, and coins to the User, confirming that the User has been successfully created.

### Postconditions:

* The new User has been created and their information has been saved to the database.
* The User has received confirmation of the new User's unique ID, level, and coins.


### Acceptance Criteria:

* I can access the "Create User" feature from the main menu or home page.
* The "Create User" form asks me for my name and any other relevant information, and I can submit it without any errors.
* If my name is unique and not already in use, the System creates a new User with my provided information and default values.
* I receive confirmation of my new User's unique ID, level, and coins, so that I know I have successfully created an account.
* If my name is not unique, the System notifies error

## Title: Update User Progress

### User Story:
As a player, I want my progress to be updated after I win a level, so that I can keep track of my progress and advance to the next level with increased rewards.

### Preconditions:

The user has already been created and has a valid authentication token.

### Postconditions:

The user's progress is updated with the new level and coins earned.
The updated progress is returned to the user without the authentication token.

### Flow:

* The player wins a level.
* The client sends a request to the server to update the user's progress, including the user's authentication token.
* The server verifies the authentication token to ensure the user is valid.
* If the authentication token is invalid, the server returns an error message.
* If the authentication token is valid, the server retrieves the user's current progress from the database.
* The server updates the user's level by incrementing it by 1 and updates the user's coins by adding 25.
* The server saves the updated user progress to the database.
* The server returns the updated progress data to the client without the authentication token.

## Title: Create Team And Join Team
### Description
The Create Team service allows a user to create a new team or join an existing team.
### User Story
As a user, I want to create a new team or join an existing team so that I can collaborate with other users and participate in team-based activities. When creating a new team, I should be able to choose a unique team name. When creating a new team or join team, the system should deduct 1,000 coins from my account. When joining an existing team, I should be able to specify the name of the team and the system should add me to the team if it exists and has not reached the maximum capacity of 20 users.

### ACTORS
* User
* System

### Preconditions
The user is logged in, has at least 1,000 coins and has not team.
### Flow of Events
*The user selects the option to create a new team or join an existing team.
* If the user chooses to create a new team:
    * The user enters a unique team name.
    * The system verifies that the team name is unique.
    * The system deducts 1,000 coins from the user's account.
    * The system creates a new team with the specified name and adds the user to it.
    * The system returns the created team details to the user.

* If the user chooses to join an existing team:
    * The user enters the name of the team they want to join.
    * The system verifies that the team exists and has not reached the maximum capacity of 20 users.
    * The system deducts 1,000 coins from the user's account.
    * The system adds the user to the specified team.
    * The system returns the updated team details to the user.

### Postconditions
The user has either created a new team or joined an existing team.
The user's account balance has been updated accordingly.


## Title: Leave Team

### User Story:
As a user who is currently in a team, I want to leave the team so that I can either join a different team or create my own team. When I request to leave the team, the system should validate my token and confirm that I am currently in a team. If I am the only member of the team, the system should remove me from the team, delete the team, and update my team status to null. If there are other members in the team, the system should remove me from the team and save the updated team object to the repository. Either way, I should receive confirmation that I have successfully left the team.

### Actor:
* User

### Preconditions:
User is currently in a team and has a valid token

### Postconditions:
User has left the team and if the user was the team leader and was the only member, the team is deleted

### Flow of Events:

* User initiates the leaveTeam request with their token
* The system validates the user's token and confirms that the user is currently in a team
* If the user is the only member of their team, the system removes the user from the team, deletes the team, and updates the user's team status to null. The service then ends.

* If the user is not the only member of their team, the system retrieves the team object from the repository using the user's team ID
* The system removes the user from the team and saves the updated team object to the repository
* The service ends


