#!/bin/bash
# /var/lib/docker/volumes/wildfly_wildfly-mgmt/_data/standalone/deployments/
docker-compose -f wildfly-compose.yml up --build -d
