# Final image for usage.
FROM adoptopenjdk:11-jre-hotspot

# Changes workdir.
WORKDIR app

# Downloads hpo.owl file.
ARG hpo_owl_download
RUN curl -O -L ${hpo_owl_download}

# Downloads & prepares TDB.
ARG vibe_tdb_download
ARG vibe_tdb_archive
RUN curl -O -L ${vibe_tdb_download} \
&& tar -xzf ${vibe_tdb_archive} \
&& rm ${vibe_tdb_archive} \
&& mv ${vibe_tdb_archive%.tgz} TDB

# Retrieves jar file.
ARG jar_file
ADD target/${jar_file} .

# Workaround for env variables & supporting custom arguments when running container.
# https://stackoverflow.com/questions/49133234/docker-entrypoint-with-env-variable-and-optional-arguments
RUN echo "java -jar ${jar_file} -t TDB/ -w hp.owl \$@" > run.sh

# Runs java app.
ENTRYPOINT ["/bin/bash", "run.sh"]
CMD ["--help"]