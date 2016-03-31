FROM ubuntu

# Java

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y  software-properties-common && \
    add-apt-repository ppa:webupd8team/java -y && \
    apt-get update && \
    echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
    apt-get install -y oracle-java8-installer && \
    apt-get clean

RUN apt-get install -y -f git wget unzip 
RUN mkdir /var/data
RUN mkdir /var/data/agg

RUN wget https://dl.bintray.com/sbt/native-packages/sbt/0.13.11/sbt-0.13.11.zip
RUN unzip sbt-0.13.11
RUN rm sbt-0.13.11.zip
RUN mv sbt /opt/sbt
ENV PATH $PATH:/opt/sbt/bin

WORKDIR /
RUN git clone https://github.com/jpzk/tracking-api.git
WORKDIR /tracking-api
RUN sbt assembly
RUN mv /tracking-api/target/scala-2.10/tracking-api.jar /
RUN mv /tracking-api/application.conf /
RUN mv /tracking-api/docker/start.sh /
RUN chmod +x /start.sh

EXPOSE 9990
EXPOSE 8888

WORKDIR /
ENTRYPOINT ["/bin/bash","/start.sh"]

