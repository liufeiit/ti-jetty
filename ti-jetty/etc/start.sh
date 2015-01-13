#!/bin/sh

java -Dsun.net.inetaddr.ttl=60 -Xmx256M -XX:+HeapDumpOnOutOfMemoryError -DLOG_DIR=/log/java/af-cfc-yeepay/log -DSLA_LOG_DIR=/log/java/af-cfc-yeepay/sla-log -jar ti-jetty-1.0.0-Final.jar lib/*


