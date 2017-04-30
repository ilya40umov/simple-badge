# Automates common operations

SHELL := bash

.PHONY: test
DEFAULT_TARGET: build

build:
	@mvn clean package
