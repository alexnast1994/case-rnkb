FROM openjdk:8-jdk-alpine
WORKDIR /tmp

ADD target/*.jar /tmp
ADD application.sh /tmp

ENTRYPOINT exec java $JAVA_OPTS -jar /tmp/*.jar
CMD ["sh", "application.sh"]