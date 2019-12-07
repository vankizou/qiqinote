#!/bin/sh
#nohup java -Xmx512M -XX:+UseG1GC -jar -Dspring.config.location=/data/wt/client/www/config.properties -Dlog4j.configuration=file:/data/wt/client/www/log.properties /data/wt/client/www/zhuge-client-web-1.0.jar > /dev/null 2>&1 &
nohup java -Xmx512M -XX:+UseG1GC -jar -Dspring.config.location=./config.properties ./qiqinote-web-1.0.jar > /dev/null 2>&1 &