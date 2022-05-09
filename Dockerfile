FROM openjdk:11
ADD target/docker-spring-boot.jar docker-spring-boot.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "docker-spring-boot.jar"]
#CMD ["java", "-cp", "docker-spring-boot.jar", "com/example/springboot/SpringbootBackendApplication"]

# create sql server
# create docker registery
# create web app for container

# mvn clean install
# docker build -f Dockerfile -t hrithikhs/springboot-backend .
# docker rmi <IMGID>
# docker images
# docker run -p 8090:8090 hrithikhs/springboot-backend
# docker login speproject.azurecr.io  Username Password -> Access keys azure
# docker push hrithikhs/springboot-backend
#