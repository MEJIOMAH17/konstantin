FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR /
ADD build/libs/server-all.jar server.jar
EXPOSE 8080
CMD java -jar server.jar