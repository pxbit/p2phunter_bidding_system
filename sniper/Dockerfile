FROM openjdk:8-jdk-alpine
MAINTAINER cfig <yuyezhong@gmail.com>

VOLUME /tmp
ARG JAR_FILE
RUN apk add --no-cache tzdata
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY rj /bin/
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["rj", "/app.jar"]
