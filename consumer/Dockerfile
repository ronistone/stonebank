FROM azul/zulu-openjdk:11

RUN mkdir /consumer

COPY ./target/consumer-0.0.1-SNAPSHOT.jar /consumer/consumer.jar

WORKDIR /consumer

ENTRYPOINT [ "java", "-jar", "./consumer.jar" ]