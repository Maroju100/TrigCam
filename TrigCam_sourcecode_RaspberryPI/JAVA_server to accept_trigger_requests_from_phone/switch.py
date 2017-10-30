import RPi.GPIO as GPIO
import time
import os
import subprocess
import sys
import signal
from datetime import datetime

GPIO.setmode(GPIO.BCM)
GPIO.setup(18, GPIO.IN, pull_up_down=GPIO.PUD_UP)
state= 'True'

#def my_callback(channel):
#    global start
#    start = 0;
#    global end
"""    end = 0; 
    if GPIO.input(18) == 1:
        start = time.time()
    if GPIO.input(18) == 0:
	end = time.time()
	elapsed = end - start
        print(elapsed)
GPIO.add_event_detect(18, GPIO.BOTH, callback=my_callback, bouncetime=200)
"""

while True:
	input_state = GPIO.input(18)
	#print ('Button not Pressed')	
#	GPIO.add_event_detect(18, GPIO.BOTH, callback=my_callback, bouncetime=200)
	if input_state == False:
#	   print('I am after first if')
	   if state == 'True':
	    print('Button Pressed')
	    print('Video recording started')
            cmd = 'raspivid -o video.h264 -t 10000'
#	    cmk='ls -l'
	    i=datetime.now()
	    filename1=i.strftime('%Y_%m_%d_%H_%M_%S')
	    #filename1= (time.strftime("%H_%M_%S"))	
	    filename2= 'video.h264%04d'
	    filename3='/home/pi/Sync/'
	    filename=filename1+filename2
	    filename=filename3+filename	
       	    #os.system(cmd &)
	    popen=subprocess.Popen(['raspivid','-o', filename,'-t', '0','-w', '640', '-h', '480','-fps','15', '--segment','1200000'])
	    time.sleep(0.2)
	    state = 'False'
	   else:
	    # state == 'False'
            # os.system('ls')
	    popen.send_signal(signal.SIGINT)
            #callback_data.abort=0;
	    print('Video recording stopped')
#	    print('Video conversion started')
#	    popen=subprocess.Popen('sh', 'conversion.sh');
	    state='True'
	    time.sleep(0.2)
           
	     	
