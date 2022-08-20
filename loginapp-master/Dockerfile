FROM tomcat:8
#Take the war file and copy to webapps of tomcat 
USER root
CMD microdnf install vim
CMD  apt update -y
#CMD  install docker -y
EXPOSE 8080
COPY target/*.war /usr/local/tomcat/webapps
