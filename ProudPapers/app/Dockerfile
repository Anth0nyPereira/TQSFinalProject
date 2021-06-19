FROM maven:3.8.1-jdk-11 AS build_stage
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

FROM openjdk:11-jre
WORKDIR /app
COPY --from=build_stage /app/target/proud-papers-0.0.1-SNAPSHOT.jar /app
EXPOSE 8085
CMD ["java", "-jar","proud-papers-0.0.1-SNAPSHOT.jar"]
