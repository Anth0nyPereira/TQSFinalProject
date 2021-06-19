FROM adoptopenjdk/openjdk11:latest
VOLUME /tmp
COPY proud-papers-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT [ "java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar" ]
