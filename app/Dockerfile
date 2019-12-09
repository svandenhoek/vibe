# Defines build image to use.
FROM maven:3.6.3-jdk-11-slim AS build

# Changes workdir.
WORKDIR app

# Copies pom file and builds all dependencies for offline use.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copies source and builds jar.
COPY src/main/ src/main/
RUN mvn -e -B clean package

######## ######## ########

# Final image for usage.
FROM adoptopenjdk:11-jre-hotspot

# Changes workdir.
WORKDIR app

# Downloads & prepares TDB.
ARG vibe_tdb_archive=vibe-v2_0_0-tdb.tar.gz
RUN curl -O -L https://molgenis.org/downloads/vibe/${vibe_tdb_archive} \
&& tar -xzf ${vibe_tdb_archive} \
&& mv ${vibe_tdb_archive} TDB

# Downloads hpo.owl file.
RUN curl -O -L https://raw.githubusercontent.com/obophenotype/human-phenotype-ontology/v2019-11-08/hp.owl

# Retrieves jar to be used.
COPY --from=build app/target/dependency-jars/ dependency-jars/
COPY --from=build app/target/vibe.jar .

# Runs java app.
ENTRYPOINT ["java", "-jar", "/app/vibe.jar", "-t TDB/"]
CMD ["--help"]