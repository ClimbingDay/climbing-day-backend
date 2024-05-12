FROM openjdk:21
RUN mkdir -p /opt/climbing_day/config
COPY build/libs/member.jar /opt/climbing_day/member.jar
ENTRYPOINT ["java","-jar","/opt/climbing_day/member.jar"]