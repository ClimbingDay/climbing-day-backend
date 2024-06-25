FROM openjdk:21
RUN mkdir -p /opt/climbing_day/config
COPY build/libs/event.jar /opt/climbing_day/event.jar
ENTRYPOINT ["java","-jar","/opt/climbing_day/event.jar"]