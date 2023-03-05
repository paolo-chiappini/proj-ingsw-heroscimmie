FROM maven:3.9.0-eclipse-temurin-17-focal

# Copy source folder
COPY src src

# Copy maven pom.xml
COPY pom.xml .

# Build the package
RUN mvn clean package

# Run the code
ENTRYPOINT ["java","-jar","target/ingsoft-2023-1.0-SNAPSHOT-jar-with-dependencies.jar"]