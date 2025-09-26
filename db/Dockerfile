# Use MySQL 8.0 instead of latest (more stable)
FROM mysql:8.0
# Set the working directory
WORKDIR /tmp
# Copy the entire test_db directory first
COPY db/test_db/ /tmp/test_db/
# Copy all the files to the working directory of the container
COPY db/test_db/*.sql /tmp/
COPY db/test_db/*.dump /tmp/
# Copy the main SQL file to docker-entrypoint-initdb.d.
# Scripts and SQL files in this folder are executed on container startup.
# This is specific to MySQL.
COPY db/test_db/employees.sql /docker-entrypoint-initdb.d/employees.sql
# Set the root password
ENV MYSQL_ROOT_PASSWORD example