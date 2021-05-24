FROM tomcat:8.5.50-jdk8-openjdk

ARG war_file
ARG context

COPY ${war_file} /usr/local/tomcat/webapps/${context}.war

