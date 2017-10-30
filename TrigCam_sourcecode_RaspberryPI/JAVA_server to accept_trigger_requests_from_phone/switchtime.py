import time
import RPi.GPIO as GPIO
import os

PIN=18

GPIO.setmode(GPIO.BCM)
GPIO.setup(PIN, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)

while True:
	GPIO.wait_for_edge(PIN, GPIO.RISING)
	print "Pressed"	
	start=time.time()
	time.sleep(0.2)
	
	while GPIO.input(PIN)==GPIO.HIGH:
		time.sleep(0.02)
	length=time.time()-start
	print length
	if length>5:	
		print "Long Press"
	elif length>1:
		print "Medium Press"
	else:
		print "Short Press"

