FROM azul/zulu-openjdk:11

RUN mkdir /application

COPY ./target/application-0.0.1-SNAPSHOT.jar /application/application.jar

WORKDIR /application

ENTRYPOINT [ "java", "-jar", "./application.jar" ]