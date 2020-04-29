FROM java:8
RUN echo "Asia/Shanghai" > /etc/timezone
ADD *.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar","/app.jar"]

