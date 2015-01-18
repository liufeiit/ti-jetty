#!/bin/sh

mvn clean install -Dmaven.test.skip=true
rm -r build/lib/*
cp -r build/lib jetty/
mv jetty/lib/ti-jetty-1.0.0-Final.jar jetty/bin/
cp etc/*.sh jetty/bin/
cp etc/*.xml jetty/etc/

echo "Build Jetty Success."


