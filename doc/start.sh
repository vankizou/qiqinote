#!/bin/sh
nohup java -jar -Dspring.config.location=/data/vanki/www/config.properties web-1.0-SNAPSHOT.war > /dev/null 2>&1 &