FROM openjdk:17-jre
COPY build/libs/hyoii-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
