FROM openjdk:17-alpine
RUN ./gradlew clean build -x test
ADD /build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]