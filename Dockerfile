FROM openjdk:17-alpine as builder
RUN mkdir -p /app/source
COPY . /app/source
WORKDIR /app/source
RUN ./gradlew clean build -x test


FROM openjdk:17-alpine as runtime
COPY --from=builder /app/source/build/libs/*.jar /app/app.jar
EXPOSE 587/tcp
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

