FROM gradle:7.5.0-jdk17
WORKDIR /opt/app

# Copy all backend source files
COPY . .

# Build the JAR file
RUN gradle build

# Use the generated JAR file
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar build/libs/fitness-app-0.0.1-SNAPSHOT.jar"]
