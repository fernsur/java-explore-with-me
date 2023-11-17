FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar ewm-app.jar
ENTRYPOINT ["java","-jar","/ewm-app.jar"]