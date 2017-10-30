import os
f=os.popen('ifconfig wlan0 | grep "inet\ addr" | cut -d: -f2 | cut -d" " -f1')
your_ip=f.read()
print your_ip
