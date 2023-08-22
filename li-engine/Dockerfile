FROM amazoncorretto:17
COPY target/li-engine-0.0.14-SNAPSHOT-shaded.jar /tmp
ENV MAX_SIMULTANEOUS_GAMES=2
ENV MAX_DEFAULT_DEPTH=6
ENTRYPOINT ["java","-jar","/tmp/li-engine-0.0.14-SNAPSHOT-shaded.jar"]
