FROM openjdk:11
MAINTAINER samanalishiri@gmail.com

# Build arguments
ARG USER=artemis
ARG GROUP=ops
ARG USER_HOME=/home/$USER
ARG WORK_DIRECTORY=/opt
ARG APP_HOME=$WORK_DIRECTORY/artemis
ARG APP_VERSION=2.29.0
ARG BROKER_HOME=$WORK_DIRECTORY/broker

# environment variable
ENV USER=$USER
ENV GROUP=$GROUP
ENV HOME=$USER_HOME
ENV WORK_DIRECTORY=$WORK_DIRECTORY
ENV APP_HOME=$APP_HOME
ENV APP_VERSION=$APP_VERSION
ENV BROKER_HOME=$BROKER_HOME
ENV APP_USER=artemis
ENV APP_PASSWORD=artemis
# there are two options for ANONYMOUS_LOGIN
# 1: --require-login
# 2: --allow-anonymous
ENV ANONYMOUS_LOGIN="--allow-anonymous"
ENV HTTP_HOST="0.0.0.0"
ENV EXTRA_ARGS="--relax-jolokia"

RUN groupadd $GROUP
RUN useradd -d /home/$USER -g $GROUP -m -s /bin/bash $USER

RUN mkdir -p  $APP_HOME
RUN chown -R $USER:$GROUP $APP_HOME

RUN mkdir $BROKER_HOME
RUN chown -R $USER:$GROUP $BROKER_HOME

# update OS
RUN apt-get update
RUN apt-get install -y curl
RUN apt-get install -y libaio1
RUN rm -rf /var/lib/apt/lists/*
# download and install apache active mq artemis
RUN curl -L https://downloads.apache.org/activemq/activemq-artemis/$APP_VERSION/apache-artemis-$APP_VERSION-bin.tar.gz -o artemis.tar.gz
RUN tar -xzf artemis.tar.gz --strip-components=1 -C $APP_HOME
RUN rm artemis.tar.gz

COPY ./start.sh $WORK_DIRECTORY
RUN chmod 755 -R $WORK_DIRECTORY/start.sh

# Web Server
EXPOSE 8161 \
# JMX Exporter
    9404 \
# Port for CORE,MQTT,AMQP,HORNETQ,STOMP,OPENWIRE
    61616 \
# Port for HORNETQ,STOMP
    5445 \
# Port for AMQP
    5672 \
# Port for MQTT
    1883 \
#Port for STOMP
    61613

USER artemis

VOLUME ["$APP_HOME", "$BROKER_HOME"]
CMD $WORK_DIRECTORY/start.sh
