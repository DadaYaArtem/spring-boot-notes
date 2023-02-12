FROM openjdk:17-alpine
RUN gradle build
ADD /build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
