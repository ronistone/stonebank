FROM azul/zulu-openjdk:11

RUN mkdir /processor

COPY ./target/processor-0.0.1-SNAPSHOT.jar /processor/processor.jar

WORKDIR /processor

ENTRYPOINT [ "java", "-jar", "./processor.jar" ]