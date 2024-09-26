# Getting Started

### To Run In Docker
To build the docker file: `docker save webflux_customer_test -o ./webflux.tar`
To load the included docker image into your Docker instance: `docker load -i webflux.tar`
To start up the application with port 8080 exposed: `docker run -p 8080:8080 webflux_customer_test`

### Tests
Tests should not require any external dependencies in order to run.
Execute by running: `./gradlew tests`

### Sample Commands
Sample curl commands are provided in the examples folder.
These commands can be run from the command-line (if curl is installed) or they can be imported into a utility like Postman.