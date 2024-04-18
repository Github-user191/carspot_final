FROM maven:3.8.6-openjdk-11 as build

# Set the current working directory inside the image
WORKDIR /app

# Copy maven executable to the image
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .
# Build all the dependencies in preparation to go offline.
# This is a separate step so the dependencies will be cached unless
# the pom.xml file has changed.
RUN mvn dependency:go-offline -B

# Copy the project source
COPY src src

# Package the application
RUN mvn package
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

#### Stage 2: A minimal docker image with command to run the app
FROM maven:3.8.6-openjdk-11

ARG DEPENDENCY=/app/target/dependency

# Copy project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 9090
ENTRYPOINT ["java","-cp","app:app/lib/*","com.carspot.app.BackendApplication"]
#
#FROM openjdk:11-jre-slim
#
#WORKDIR /app
#COPY mvnw .
#COPY .mvn .mvn
#COPY pom.xml .
#COPY src src
#
#RUN mvn install -DskipTests
#EXPOSE 8080
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]