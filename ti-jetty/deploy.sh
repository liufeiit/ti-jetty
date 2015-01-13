#!/bin/sh

mvn clean install -Dmaven.test.skip=true

scp -r build/lib andpay@stage0_cfc:~/app/lc/jetty/


