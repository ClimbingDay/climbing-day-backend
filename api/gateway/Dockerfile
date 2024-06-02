FROM openjdk:21
RUN mkdir -p /opt/climbing_day/config
COPY build/libs/gateway.jar /opt/climbing_day/gateway.jar
ENTRYPOINT ["java","-jar","/opt/climbing_day/gateway.jar"]