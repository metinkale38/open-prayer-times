FROM gradle:7.6-jdk21 AS builder
WORKDIR /app
COPY . ./
RUN chmod +x gradlew
RUN ./gradlew :server:buildFatJar -x test


FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/server/build/libs/server-all.jar server.jar
EXPOSE 8080
CMD ["java", "-jar", "server.jar"]