FROM gradle:5.1.0-jdk11
USER root
COPY . ./
RUN gradle shadowJar
RUN java -jar build/libs/gradle.jar
