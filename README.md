# customer-statement-processor

What is this repository for?
Quick summary

Rabobank receives monthly deliveries of customer statement records. This information is delivered in JSON Format.
These records need to be validated.


Implement a REST service which receives the customer statement JSON as a POST data, Perform the below validations
 a) All transaction references should be unique
 b) The end balance needs to be validated ( Start Balance +/- Mutation = End Balance )

1. Clone the repository

https://github.com/ramgandham15/customer-statement-processor.git

2. Run the app using maven
cd customer-statement-processor
mvn spring-boot:run

The application can be accessed at http://localhost:8082.

That's it! The application can be accessed at http://localhost:8082/swagger-ui.html.

You may also package the application in the form of a jar and then run the jar file like so -

mvn clean package
java -jar target/customer-statement-processor-0.0.1-SNAPSHOT.jar
