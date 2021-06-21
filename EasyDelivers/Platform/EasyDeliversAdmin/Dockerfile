FROM maven:3.8.1-jdk-11 AS build_stage
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

FROM openjdk:11-jre
WORKDIR /app
COPY --from=build_stage /app/target/EasyDeliversAdmin-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
ENV DB_URL jdbc:mysql://db:3306/easydelivers
CMD java -jar -Dspring.datasource.url=$DB_URL EasyDeliversAdmin-0.0.1-SNAPSHOT.jar