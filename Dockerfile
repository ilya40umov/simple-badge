FROM ubuntu:16.04
MAINTAINER Illia Sorokoumov <ilya.sorokoumov@gmail.com>

ENV DEBIAN_FRONTEND noninteractive

# TODO pass in the root password from outside (e.g. via ARG)

RUN echo "mysql-server mysql-server/root_password password root123" | debconf-set-selections && \
    echo "mysql-server mysql-server/root_password_again password root123" | debconf-set-selections && \
    apt-get update && apt-get install -y mysql-server mysql-client && rm -rf /var/lib/apt/lists/*

RUN apt-get update && apt-get install -y openjdk-8-jdk && rm -rf /var/lib/apt/lists/*

COPY target/simple-badge.jar /opt/simple-badge/simple-badge.jar

# TODO potentially create a new user in mysql?

ENV BADGE_MYSQL_USERNAME root
ENV BADGE_MYSQL_PASSWORD root123

EXPOSE 8080

CMD service mysql start && sleep 3 && mysql -u root --password=root123 --execute="create database simple_badge" && \
    java -jar /opt/simple-badge/simple-badge.jar
