# Automates common operations

SHELL := bash

.PHONY: test
DEFAULT_TARGET: build

build: maven-build docker-build

maven-build:
	@mvn clean package

docker-build:
	@sudo docker build -t simple-badge .

run:
	@sudo docker run -p 8080:8080 -it --rm simple-badge
