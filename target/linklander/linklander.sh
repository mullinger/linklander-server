#!/bin/bash

# Load config into the current shell environment
source ./linklander-config.sh

#if [ "$1" = "start" ] ; then
	./tomee/bin/catalina.sh run
#elif [ "$1" = "stop" ] ; then
	#./tomee/bin/catalina.sh stop
#fi
