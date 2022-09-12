# Project pvp22 grupp 10


## Setup
### First time
1. Install postgres
2. Create a user in psql pvp_user:pvp_password (`CREATE ROLE pvp_user LOGIN PASSWORD 'pvp_password';`)
3. Create a database called pvp (`CREATE DATABASE pvp;`)
4. Grant all permissions to pvp_user (`GRANT ALL ON DATABASE pvp to pvp_user;`)
5. Run the migrations in the `api/src/main/resources/db/migrations`


Continue the setup with 
1. Go to the `utils` folder and run `mvn clean install` (this command needs to executed every time there is a change in the `utils` folder)
2. Go to the `api` folder and start the api
3. Go to the `cashier` folder and run the application (doesn't need the api currently)
