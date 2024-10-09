FROM gradle:7.5.0-jdk17
WORKDIR /opt/app
COPY ./build/libs/fitness-app-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar fitness-app-0.0.1-SNAPSHOT.jar"]