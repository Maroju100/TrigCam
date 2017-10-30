#!/bin/sh 
DATE="`date +%m`_`date +%d`_`date +%y`_`date +%T`.h264" 
#touch $DATE

case "$(pidof raspivid | wc -w)" in
0) echo "Starting Video Recording RaspiVid"
raspivid -o /home/pi/Sync/$DATE -t 20000 -w 640 -h 480 -fps 10 
#ps aux | grep =i vlc | {vlc | awk { 'print $2'} | xargs kill -9 
#exit;
;;
#touch DATE
1) echo "Video Recording already running"
;;
esac
