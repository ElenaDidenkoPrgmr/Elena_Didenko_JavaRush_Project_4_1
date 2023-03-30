# ToDo Jakarta EE Application

# Description
This is a simple application that uses Jakarta EE and Jersey to create a RESTful web service with authentication and authorization. Application work with data in JSON format. This application has several API methods for creating, reading, updating, and deleting resources related to ToDo tasks for Users. All data is stored in Postgres DB.
JWT (JSON Web Tokens) are used for authentication and authorization, and Liquibase is used for database migrations. Jakarta Validation is used for input validation, and passwords are hashed using the bcrypt algorithm.

# Usage
The application has several API methods accessible through HTTP requests. All requests and responses have a JSON format.

# Creating a User
To create a new User, send a POST request to /users with the request body in JSON format:

```json
{
"name": "John Doe",
"email": "john.doe@example.com",
"password": "123456"
}
```
The response will be returned in JSON format and will contain the ID of the created User:

```json
{
"id": 1,
"name": "John Doe",
"email": "john.doe@example.com",
"password": "123456"
}
```
# Creating a Task
To create a new task for a User, send a POST request to /users/{userId}/tasks with the request body in JSON format:

```json
{
"title": "Task 1",
"description": "This is the first task"
}
```
The response will be returned in JSON format and will contain the ID of the created task:

```json
{
"id": 1,
"title": "Task 1",
"description": "This is the first task",
"completed": false,
"user_id": 1
}
```
# Application API
This page with Application API hosted on SwaggerHub [Swagger API ToDo](https://app.swaggerhub.com/apis-docs/elena.didenko.prgmr/ToDo/1.0.0).


# Technologies Used
- Jakarta EE: A set of specifications and APIs for enterprise Java applications.
- Jersey: An open-source framework for building RESTful web services in Java.
- Hibernate: open source object relational mapping (ORM) tool
- JWT: A standard for creating secure access tokens that can be used for authentication and authorization.
- Liquibase: A database migration tool that allows you to manage changes to your database schema over time.
- Jakarta Validation: A validation framework for Java that allows you to define constraints on input data.
- Password hash: A technique for securely storing passwords by hashing them with a one-way function.
- Swagger: an open-source software framework that helps developers design, build, document, and consume RESTful web services.
- Log4j2: logging framework for Java applications.
