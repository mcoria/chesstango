FROM amazoncorretto:17
COPY target/li-engine-0.0.21-shaded.jar /tmp
ENV MAX_SIMULTANEOUS_GAMES=2
ENV CHALLENGE_BOTS=true
ENTRYPOINT ["java","-jar","/tmp/li-engine-0.0.21-shaded.jar"]
