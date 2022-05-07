
## Installation
In order to run this project.
Open a terminal and navigate to project directory
and run the following commands 
- mvn clean install
- docker-compose build
- docker-compose up

## Test the application
Navigate to
[Home app](http://localhost:8080) `http://localhost:8080`
Open two instances
Connect with a name and start sending messages to each other.

### Testing

Tests for websocket endpoint are ignored since there is a problem with threads,
i didn't have time to figure it out.
Tests for message-service are implemented
### Rest Endpoints
#### Version
Navigate to swagger through [Swagger](http://localhost:8080/swagger-ui/index.html)
and get the version of the project through the corresponding endpoint.

Otherwise, navigate to [Version](http://localhost:8080/version) endpoint
`http://localhost:8080/version`
#### Status
Navigate to [Status](http://localhost:8080/actuator/health) endpoint to check the status of the application
`http://localhost:8080/actuator/health`
