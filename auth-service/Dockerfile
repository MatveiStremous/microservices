FROM openjdk:19

WORKDIR /app

COPY build/libs/auth-service-0.0.1-SNAPSHOT.jar auth.jar

EXPOSE 8082

CMD ["java", "-jar", "auth.jar"]