#!/bin/sh 
DATE="`date +%m`_`date +%d`_`date +%y`_`date +%T`.h264" 
#touch $DATE


raspivid -o $DATE -t 20000 -w 640 -h 480 -fps 10 
#touch DATE
