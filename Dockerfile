FROM --platform=amd64 openjdk:17.0.2-oraclelinux8
COPY . ./app
ENTRYPOINT ["java","-jar","app/target/ecommerce-0.0.1-SNAPSHOT.jar"]
