FROM openjdk:11
EXPOSE 8080
ADD target/chart-visualizer.jar chart-visualizer.jar
ENTRYPOINT ["java","-jar","/chart-visualizer.jar"] 