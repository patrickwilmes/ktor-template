FROM openjdk:18
EXPOSE 8080:8080
RUN mkdir /app
COPY /boot/build/libs/boot-all.jar /app/server.jar
ENTRYPOINT ["java","-jar","/app/server.jar"]
