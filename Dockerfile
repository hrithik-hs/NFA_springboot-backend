FROM openjdk:11
ADD target/docker-spring-boot.jar docker-spring-boot.jar
EXPOSE 8090
CMD ["java", "-cp", "docker-spring-boot.jar", "com/example/springboot/SpringbootBackendApplication"]