FROM eclipse-temurin:21-jre-alpine

COPY ./build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "-Duser.timezone=UTC", "app.jar"]

EXPOSE 8081
