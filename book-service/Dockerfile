FROM openjdk:19

WORKDIR /app

COPY build/libs/book-service-0.0.1-SNAPSHOT.jar book-service.jar

EXPOSE 8080

CMD ["java", "-jar", "book-service.jar"]