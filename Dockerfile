FROM --platform=amd64 openjdk:17.0.2-oraclelinux8
LABEL authors="minhnguyen"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
