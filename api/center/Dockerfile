FROM openjdk:21
RUN mkdir -p /opt/climbing_day/config
COPY build/libs/center.jar /opt/climbing_day/center.jar
ENTRYPOINT ["java","-jar","/opt/climbing_day/center.jar"]