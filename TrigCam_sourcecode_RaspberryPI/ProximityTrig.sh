#!/bin/bash

case "$(pidof ProximityTest2 | wc -w)" in
0) echo "Starting Prxomity Sensing"
#cd /home/pi/TrigCam-apds-9960/
cd /home/pi
./ProximitySensing2
#raspivid -o /home/pi/Sync/$DATE -t 20000 -w 640 -h 480 -fps 10 
#ps aux | grep =i vlc | {vlc | awk { 'print $2'} | xargs kill -9 
#exit;
;;
#touch DATE
1) echo "Proximity  Sensing already running"
;;
esac
                


#./ProximityTest2
