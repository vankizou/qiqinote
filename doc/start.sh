#!/bin/sh
nohup java -Xms256M -Xmx1024M -XX:PermSize=256M -XX:MaxPermSize=1024M -jar -Dspring.config.location=/data/vanki/www/config.properties web-1.0-SNAPSHOT.war > /dev/null 2>&1 &