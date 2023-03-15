# Notes App
Notes app with Docker, Java, Spring Boot, PostgreSQL.

# Requirements
Docker

# Installation steps
- Download project files
- In project folder open terminal and execute "docker-compose up"
- to use email verification feautire you need to change mail.username and mail.password in applciation.properties to your smtp information or use provided by me for TEST ONLY
# URL address
- localhost:8080
# Endpoints
- "/" -start page redirects to login
- "/login" - login in existing account
- "/register" - create a new account
- "/logout" - log out of your account
- "/note/list" - list of your notes
- "/note/create" - create new note
- "/note/edit/{id}" - edit chosen note
- "/note/share/{id}" - page of chosen note, if note is public, its url is copied and can be send to another user
