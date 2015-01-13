#!/bin/sh

nohup sh start.sh > nohup.out 2>&1 &

tail -f nohup.out /log/java/af-cfc-yeepay/log/* /log/java/af-cfc-yeepay/sla-log/*
