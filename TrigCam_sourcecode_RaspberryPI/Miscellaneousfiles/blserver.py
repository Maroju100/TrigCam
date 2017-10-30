import os
import glob
import time
#import RPi.GPIO as GPIO
from bluetooth import *

#os.system('modprobe w1-gpio')
#os.system('modprobe w1-therm')

#GPIO.setmode(GPIO.BCM)
#GPIO.setup(17, GPIO.OUT)

#base_dir = '/sys/bus/w1/devices/'
#device_folder = glob.glob(base_dir + '28*')[0]
#device_file = device_folder + '/w1_slave'

def read_temp_raw():
    f = open(device_file, 'r')
    lines = f.readlines()
    f.close()
    return lines

def read_temp():
    lines = read_temp_raw()
    while lines[0].strip()[-3:] != 'YES':
        time.sleep(0.2)
        lines = read_temp_raw()
    equals_pos = lines[1].find('t=')
    if equals_pos != -1:
        temp_string = lines[1][equals_pos+2:]
        temp_c = float(temp_string) / 1000.0
        temp_f = temp_c * 9.0 / 5.0 + 32.0
        return temp_c
	
#while True:
#	print(read_temp())	
#	time.sleep(1)

f=os.popen('ifconfig wlan0 | grep "inet\ addr" | cut -d: -f2| cut -d" " -f1')
your_ip=f.read()
s=your_ip;
your_ip=str(your_ip)
print your_ip
#s =reduce(lambda a,b: a<<8 | b, map(int, s.split(".")))
#print s
server_sock=BluetoothSocket( RFCOMM )
server_sock.bind(("",PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]

uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"

advertise_service( server_sock, "AquaPiServer",
                   service_id = uuid,
                   service_classes = [ uuid, SERIAL_PORT_CLASS ],
                   profiles = [ SERIAL_PORT_PROFILE ], 
#                   protocols = [ OBEX_UUID ] 
                    )
#client_sock, client_info = server_sock.accept()
while True:          
	print "Waiting for connection on RFCOMM channel %d" % port
	client_sock, client_info = server_sock.accept()
	print "Accepted connection from ", client_info
	os.system("sudo sh ProximityTrig.sh")
	
	try:
		data='x';	
#	        data = client_sock.recv(1024)
        	if len(data) == 0: break
	        print "received [%s]" % data

		if data == 'temp':
			data = str(read_temp())+'!'
		elif data == 'lightOn':
#			GPIO.output(17,False)
			data =s
		elif data == 'lightOff':
#			GPIO.output(17,True)
			data = 'light off!'
#		        os.system("sudo sh ProximityTrig.sh")
#			data = str(your_ip)
		else:
			data = your_ip 
	        client_sock.send(data)
		print "sending [%s]" % data

	except IOError:
		pass

	except KeyboardInterrupt:

		print "disconnected"

		client_sock.close()
		server_sock.close()
		print "all done"

		break
	
