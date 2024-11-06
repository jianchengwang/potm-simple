FROM zulu-openjdk:21-jdk-crac-latest

RUN mkdir -p /app/config

WORKDIR /app

ARG JAR_FILE=build/libs/potm-svc-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /app/app.jar

COPY --from=hengyunabc/arthas:latest /opt/arthas /opt/arthas

EXPOSE 7092

ENV TZ=Asia/Shanghai JAVA_TOOL_OPTIONS="--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED"

ENTRYPOINT exec java $JAVA_TOOL_OPTIONS -jar app.jar;
