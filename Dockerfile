FROM openjdk:17
COPY target/expensetracker-api.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
