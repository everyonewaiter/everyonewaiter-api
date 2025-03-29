FROM eclipse-temurin:21-jre-alpine

COPY ./build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "app.jar"]

EXPOSE 8081
