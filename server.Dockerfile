FROM openjdk:11

WORKDIR /app
COPY ./jeenjuhs/target/server-1.0-SNAPSHOT.jar /app/server-1.0-SNAPSHOT.jar
CMD ["java", "-jar", "server-1.0-SNAPSHOT.jar"]