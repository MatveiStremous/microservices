FROM openjdk:19

WORKDIR /app

COPY build/libs/gateway-0.0.1-SNAPSHOT.jar gateway.jar

EXPOSE 8765

CMD ["java", "-jar", "gateway.jar"]