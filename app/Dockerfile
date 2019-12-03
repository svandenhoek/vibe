# Defines build image to use.
FROM maven:3.6.3-jdk-11-slim AS build

# Changes workdir.
WORKDIR app

# Copies pom file and builds all dependencies for offline use.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copies source and builds jar.
COPY src/main/ .
RUN mvn -e -B clean package

######## ######## ########

# Final image for usage.
FROM adoptopenjdk:11-jre-hotspot

# Downloads TDB.
ADD http://molgenis.org/downloads/vibe/vibe-v2_0_0-tdb.zip .

# Retrieves jar to be used.
COPY --from=build app/target/dependency-jars .
COPY --from=build app/target/vibe.jar .

# Runs java app.
#ENTRYPOINT ["java", "-jar", "vibe.jar"]
#CMD ["--help"]
CMD ["java", "--version"]