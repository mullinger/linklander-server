#!/bin/bash

if [ "$1" = "start" ] ; then
	./tomee/bin/catalina.sh start
elif [ "$1" = "stop" ] ; then
	./tomee/bin/catalina.sh stop
fi
