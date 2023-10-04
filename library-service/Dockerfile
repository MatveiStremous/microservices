FROM openjdk:19

WORKDIR /app

COPY build/libs/library-service-0.0.1-SNAPSHOT.jar library-service.jar

EXPOSE 8081

CMD ["java", "-jar", "library-service.jar"]