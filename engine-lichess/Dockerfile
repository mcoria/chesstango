FROM amazoncorretto:21
COPY target/chesstango-lichess.jar /tmp
ENV MAX_SIMULTANEOUS_GAMES=2
ENV CHALLENGE_BOTS=true
ENTRYPOINT ["java","-jar","/tmp/chesstango-lichess.jar"]
