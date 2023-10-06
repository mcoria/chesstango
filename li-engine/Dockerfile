FROM amazoncorretto:17
COPY target/li-engine-0.0.19-shaded.jar /tmp
ENV MAX_SIMULTANEOUS_GAMES=2
ENTRYPOINT ["java","-jar","/tmp/li-engine-0.0.19-shaded.jar"]
