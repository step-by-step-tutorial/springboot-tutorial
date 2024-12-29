FROM openjdk:11-jre-slim

WORKDIR /opt/h2

COPY h2-*.jar /opt/h2/h2.jar
ENV WEB_PORT=8082
ENV TCP_PORT=9092
EXPOSE $WEB_PORT $TCP_PORT

CMD java -cp h2.jar org.h2.tools.Server \
    -web -webAllowOthers -webPort $WEB_PORT \
    -tcp -tcpAllowOthers -tcpPort $TCP_PORT \
    -baseDir /opt/h2-data -ifNotExists
