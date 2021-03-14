# welearn-back-end
Distance learning project

# Prerequisites
* Maven::latest
* Docker::latest
* JDK 11

# To run locally
* mvn clean install -DskipTests 
* docker-compose -f docker-compose-database-welearn.yml up --build
* mvn spring-boot:run -P local

# To run as docker container
* mvn clean install -DskipTests
* docker-compose -f docker-compose-welearn.yml up --build
