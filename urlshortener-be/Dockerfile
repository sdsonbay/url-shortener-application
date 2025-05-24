FROM openjdk:17-jdk
WORKDIR /app
ENV PORT 80
EXPOSE 80

COPY ./target/*.jar /app/urlshortener-app.jar

ENTRYPOINT exec java $JAVA_OPTS -jar urlshortener-app.jar