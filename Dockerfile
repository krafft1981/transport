FROM openjdk:21

EXPOSE 8080

RUN mkdir /app

COPY build/libs/*.jar /app/application.jar

ENTRYPOINT ["java", "-jar","/app/application.jar"]
