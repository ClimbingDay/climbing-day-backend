FROM openjdk:21
RUN mkdir -p /opt/climbing_day/config
COPY build/libs/mail.jar /opt/climbing_day/mail.jar
ENTRYPOINT ["java","-jar","/opt/climbing_day/mail.jar"]