FROM eclipse-temurin:17-jre
EXPOSE 8080

# Environment variables for runtime configuration
ENV MYSQL_URL=""
ENV MYSQL_USERNAME=""
ENV MYSQL_PASSWORD=""
ENV EMAIL_SERVICE_URL=""
ENV JWT_SECRET=""


# Spring Boot logging configuration
ENV LOGGING_LEVEL_ROOT=INFO
ENV LOGGING_LEVEL_COM_EXAMPLE=DEBUG
ENV LOGGING_PATTERN_CONSOLE="%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"

# Create a non-root user for security
RUN groupadd --system spring && useradd --system --gid spring spring
USER spring:spring

# Build argument for JAR file name
ARG JAR_FILE
COPY build/libs/*.jar app.jar

# Use exec form with JVM options for better debugging
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.output.ansi.enabled=ALWAYS", "-Xlog:gc*:stdout:time", "-jar", "/app.jar", "--spring.profiles.active=prod", "--logging.level.root=INFO", "--logging.level.com.example=DEBUG"]