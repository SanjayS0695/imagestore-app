FROM openjdk:17-jdk-slim
WORKDIR /app
CMD ["./gradlew", "clean", "bootJar"]
COPY /build/libs/*.jar imagestore.jar
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar .
ENV JAVA_TOOL_OPTIONS "-javaagent:./opentelemetry-javaagent.jar"
ENTRYPOINT ["java", "-jar", "imagestore.jar"]