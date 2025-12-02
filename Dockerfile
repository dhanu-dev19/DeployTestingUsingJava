# Dockerfile
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

FROM tomcat:9.0-jre11-openjdk-slim

# Install psql client for health checks (optional)
RUN apt-get update && apt-get install -y postgresql-client && rm -rf /var/lib/apt/lists/*

# Remove default Tomcat apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR file from build stage
COPY --from=build /app/target/supabase-app.war /usr/local/tomcat/webapps/ROOT.war

# Create a health check script
RUN echo '#!/bin/bash\n\
pg_isready -h "${DB_HOST:-localhost}" -p "${DB_PORT:-5432}" -U "${DB_USERNAME:-postgres}" || exit 1\n\
curl -f http://localhost:8080/ || exit 1' > /healthcheck.sh && \
chmod +x /healthcheck.sh

# Expose port 8080
EXPOSE 8080

# Set environment variables
ENV CATALINA_OPTS="-Xmx512m -Djava.security.egd=file:/dev/./urandom"

# Start Tomcat
CMD ["catalina.sh", "run"]