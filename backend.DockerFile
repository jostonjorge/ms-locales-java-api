FROM amazoncorretto:17-alpine-jdk
ARG API_PATH=/home/ms-locales-api
ARG COUNTRIES_JSON_PATH=/data/json/countries
WORKDIR ${API_PATH}
ADD $COUNTRIES_JSON_PATH .$COUNTRIES_JSON_PATH
COPY target/*.jar ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]