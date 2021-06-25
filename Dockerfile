FROM openjdk:8-jdk

RUN mkdir /app
COPY . /app
WORKDIR /app
RUN ./gradlew clean installDist
EXPOSE 8080:8808

CMD ["./build/install/golfbook-api/bin/golfbook-api"]