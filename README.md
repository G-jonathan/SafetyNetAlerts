<h1>SafetyNetAlerts</h1>

The purpose of this application is to send information to emergency service systems.
The application will provide information on the people present at the scene of an incident.


<h2>Why ?</h2>

   - A fire breaks out  ! 

Provide information on the people present in the building. 

   - A hurricane is approaching !

Alert the people of the region by text message.

   - A flood occurs !

Provide emergency services with information on people in the area (age, medical history, etc.) 


<h2>How ?</h2>

It's a back-end Rest Api built with Spring Boot.

The application imports information about people (Name, first name, address, phone number, age, medication, allergies, email) from a Json file.

When the program start, the application serializes data from a json file and filled temporary an H2 database.
Endpoints allowing access to this information are now available.

The application respond to GET requests in JSON format

POST/UPDATE/DELETE requests are also available 


<h2>Built with </h2>

- java 11 https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
- Maven 3.6.3 http://maven.apache.org/
- Spring Web (Build web,uses Apache Tomcat as the default embedded container) https://mvnrepository.com/artifact/org.springframework/spring-web
- JUnit (execution of Unit Tests) https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
- JaCoCo (test coverage reports) https://mvnrepository.com/artifact/org.jacoco/jacoco-maven-plugin
- Lombok  (Java annotation library which helps to reduce boilerplate code) https://mvnrepository.com/artifact/org.projectlombok/lombok
- H2 database https://h2database.com/html/main.html
- Jackson(JSON library) https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind


<h2>Installation and Getting Started</h2>
<h3>Requirements</h3>


 - Java 11
 - Maven 3.6.3

<h3>Installation</h3>

1. Clone this repo 
   ```shell
   git clone https://github.com/G-jonathan/SafetyNetAlerts.git

2. Package the application
   ```shell
   mvn package
   
3. Execute the Jar
   ```shell
   java -jar ./target/SafetyNetAlerts-0.0.1-SNAPSHOT.jar

<h3>EndPoints</h3>
All available endpoints can be viewed at this address when the program is running.

http://localhost:8080/swagger-ui.html#/

![image](https://user-images.githubusercontent.com/72458863/124315915-0830bb00-db75-11eb-936d-6e9a3cfe36a0.png)
![image](https://user-images.githubusercontent.com/72458863/124317766-fbfa2d00-db77-11eb-9ffd-6fac92ec533c.png)
![image](https://user-images.githubusercontent.com/72458863/124317802-09171c00-db78-11eb-8654-dd92053c367d.png)


