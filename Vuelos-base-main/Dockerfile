
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn -DskipTests clean package


FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar


ENV PORT=10000
EXPOSE 10000

CMD ["sh","-c","java -Xms128m -Xmx512m -jar app.jar"]
