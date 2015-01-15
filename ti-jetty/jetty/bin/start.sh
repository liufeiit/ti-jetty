#!/bin/sh
java -Dsun.net.inetaddr.ttl=60 -Xmx256M -XX:+HeapDumpOnOutOfMemoryError -jar ti-jetty-1.0.0-Final.jar
