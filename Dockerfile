FROM ubuntu:16.04

RUN  apt-get update \
  && apt-get install -y wget \
    && rm -rf /var/lib/apt/lists/*

RUN wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-ubuntu1404-3.2.5.tgz

RUN tar zxvf mongodb-linux-x86_64-ubuntu1404-3.2.5.tgz
RUN ls
RUN cp mongodb-linux-x86_64-ubuntu1404-3.2.5/bin/mongod /usr/bin

RUN mkdir /data/
RUN mkdir /data/db
RUN mongod &

FROM java:8

RUN apt-get update
RUN apt-get install -y maven

WORKDIR /code

ADD pom.xml /code/pom.xml
RUN ["mvn", "dependency:resolve"]

ADD src /code/src

RUN ["mvn", "verify"]
RUN ["mvn", "package"]


EXPOSE 8081
CMD ["mvn", "tomcat:run", "-Dmaven.tomcat.port=8081"]
 