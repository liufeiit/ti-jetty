#!/bin/sh

java -Dsun.net.inetaddr.ttl=60 -Xmx256M -XX:+HeapDumpOnOutOfMemoryError -DLOG_DIR=/log/java/log -jar ti-jetty-1.0.0-Final.jar


