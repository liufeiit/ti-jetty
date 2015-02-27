#!/bin/sh
echo "Begin genkey ..."
keytool -genkey -alias jetty -keypass U4gFVce7AEHOc5tbkPwaDXPMAoYExF2k8LUedAa_Jq0 -keyalg RSA -keysize 1024 -validity 365 -keystore  /root/.ssl/jetty.keystore -storepass U4gFVce7AEHOc5tbkPwaDXPMAoYExF2k8LUedAa_Jq0 -dname "CN=(刘飞), OU=(NX-UNIT), O=(NX), L=(上海市), ST=(上海), C=(CN)";
echo "Genkey Success."
keytool -list  -v -keystore /root/.ssl/jetty.keystore -storepass U4gFVce7AEHOc5tbkPwaDXPMAoYExF2k8LUedAa_Jq0
echo "=========================================================================="
keytool -list  -rfc -keystore /root/.ssl/jetty.keystore -storepass U4gFVce7AEHOc5tbkPwaDXPMAoYExF2k8LUedAa_Jq0

keytool -export -alias jetty -keystore /root/.ssl/jetty.keystore -file /root/.ssl/jetty.crt -storepass U4gFVce7AEHOc5tbkPwaDXPMAoYExF2k8LUedAa_Jq0

keytool -printcert -file /root/.ssl/jetty.crt

