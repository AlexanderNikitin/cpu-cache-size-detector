FROM openjdk
COPY ./target/cpu-cache-size-detector-1.0-SNAPSHOT.jar ./app/cpu-cache-size-detector-1.0-SNAPSHOT.jar
CMD java -jar ./app/cpu-cache-size-detector-1.0-SNAPSHOT.jar
