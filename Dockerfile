FROM --platform=amd64 openjdk:17.0.2-oraclelinux8

# Copy local code to the container image.
COPY . ./app
RUN cd app; ./mvnw clean install
ENTRYPOINT ["java","-jar","app/target/ecommerce-0.0.1-SNAPSHOT.jar"]
