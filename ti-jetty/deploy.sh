#!/bin/sh

mvn clean install -Dmaven.test.skip=true

scp -r build/lib root@aliyun:~/jetty/


