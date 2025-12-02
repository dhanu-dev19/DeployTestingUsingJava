FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:9.0-jre11-openjdk-slim

# Remove default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR file
COPY --from=build /app/target/supabase-app.war /usr/local/tomcat/webapps/ROOT.war

# Create a script to print environment info on startup
RUN echo '#!/bin/bash\n\
echo "=== RENDER ENVIRONMENT INFO ==="\n\
echo "DATABASE_URL length: ${#DATABASE_URL}"\n\
echo "DB_URL: ${DB_URL:-[NOT SET]}"\n\
echo "JAVA_OPTS: ${JAVA_OPTS}"\n\
echo "RENDER_EXTERNAL_URL: ${RENDER_EXTERNAL_URL}"\n\
echo "RENDER_INSTANCE_ID: ${RENDER_INSTANCE_ID}"\n\
echo "==============================="\n\
exec catalina.sh run' > /startup.sh && chmod +x /startup.sh

EXPOSE 8080
CMD ["/startup.sh"]