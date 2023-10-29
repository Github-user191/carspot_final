FROM jenkins/jenkins:jdk11
USER root
RUN apt-get update 
RUN curl -sSL https://get.docker.com/ | sh
#RUN curl -sL https://deb.nodesource.com/setup_14.x -o setup_14.sh
#RUN sh ./setup_14.sh
RUN apt update
#RUN apt install nodejs
RUN usermod -aG docker jenkins
