#!/bin/sh

set -ex

#Docker image for server-discovery
cd server-discovery
./gradlew bootBuildImage

#Docker image for server-config
cd ../server-config
./gradlew bootBuildImage

#Docker image for server-gateway
cd ../server-gateway
./gradlew bootBuildImage

#Docker image for service-account
cd ../service-account
./gradlew bootBuildImage

#Docker image for service-customer
cd ../service-customer
./gradlew bootBuildImage

#Docker image for service-product
cd ../service-product
./gradlew bootBuildImage

#Docker image for service-order
cd ../service-order
./gradlew bootBuildImage