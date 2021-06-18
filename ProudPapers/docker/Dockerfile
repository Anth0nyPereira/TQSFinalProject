FROM adoptopenjdk/openjdk11:latest
VOLUME /tmp
COPY proupapers-0.0.1-SNAPSHOT.jar proupapers-.jar
ENTRYPOINT [ "java","-agentlib:jdwp=transport=dt_socket,server=y,address=8085,suspend=n","-Djava.security.egd=file:/dev/./urandom","-jar","/proupapers-.jar" ]