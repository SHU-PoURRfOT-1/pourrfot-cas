FROM adoptopenjdk:11-jre-hotspot
COPY build/libs/pourrfot-cas-*.jar app.jar
EXPOSE 9001
CMD ["java", "-Dcom.sun.management.jmxremote", "-jar", "app.jar"]
