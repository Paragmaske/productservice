FROM openjdk:17-jdk
EXPOSE 8080
ADD target/product-0.0.1-SNAPSHOT.jar productservice.jar
ENTRYPOINT ["java","-jar","/productservice.jar"]