FROM postgres:16

ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB=db

COPY dump.sql /docker-entrypoint-initdb.d/