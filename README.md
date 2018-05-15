# TrigCam
A multi-sensor trigger-able camera for efficient feeding monitoring.
TrigCam: A smartwatch triggered wearable camera system for feeding habit monitoring

System description

Hardware setup
TrigCam system consists of a smartwatch paired with a android smartphone that triggers the recording of the videos at raspberry pi based fisheye camera (TrigCam) only when feeding like movements are detected at the user.
Smartwatch triggers the phone and phone relays the trigger to the TrigCam to start video recording at TrigCam for 20 seconds. If multiple consecutive triggers occur in 20 seconds window then they are buffered at the TrigCam resulting in recording of the videos without gaps.

Fig 1. TrigCam System
Bluetooth is used  between the smartphone and TrigCam to relay the triggers recieved from the smartwatch that is paired with smartphone.
WiFi- TrigCam is configured as WiFi hotspot to transfer the videos recorded at the TrigCam to phone memory every 5 minutes.

Software setup
i. App running at Smartwatch
Smartwatch app allows following three methods of triggering the TrigCam:
AccelTriggering (Accelerometer)- Roll and pitch are calculated from the accelerometer values. A 3 state finite state machine is used to trigger when all the three states are coverted. 
Orientation Triggering (Magnetometer, Gyroscope and Accelerometer)-  Uses accurate Roll, pitch and azimuth information inside the 3 state FSM to trigger.
Orientation and Magnetic triggering- (Accelerometer and Magnetometer)- Uses the Roll, Pitch and Magnetic field to detect the feeding moment. Magnet holding the TrigCam to the users chest creates the magnetic field which is detected by the smartwatch magnetometer.

Fig 2. Smartwatch App

With the detection of feeding like moments the Bite/Trigger counter is incremented at the smartwatch and the same is updated at the app running at the mobile phone.

Note- Proximity sensor at the TrigCam is an optional features that can be used to trigger the recording of the videos without the need of smart watch

ii .  App running at  Mobile phone
App running on the mobile phone runs a service to receive triggers from smartwatch and relay it to TrigCam. It also sets the time of the TrigCam to keep the time of the smartwatch, mobile phone and TrigCam synchronized. 

Fig 3. SmartPhone App
iii. Scripts running at  TrigCam
Bluetooth Server script- To receive triggers from phone app to raspberry over Bluetooth
Proximity Sensing script- To trigger recording of the video based on Proximity of the hand ( Optional)
Camera record script- To record 20 seconds of video every time a trigger is received
Video conversion script- from H264 to mp4 script-convert to mobile readable format and share every 5 minutes.
Script to  push video from the TrigCam to Mobile -Sync the videos recorded with the phone in near real time over WiFi.

Fig 4. TriCam assisted with a magnet 
A magnet holds the TrigCam to the chest of the user and also aids in magnetic sensing during the Orientation and magnetic sensing Triggering.

II. TrigCam Testing description
Testing setup:
Three kinds of smartwatch triggers were tested on a subject for a week from 3-20-2018 to 3-26-2018 for 4-5 hours each day from about 10 am to 2 pm. During the testing the smartwatch and the TrigCam was worn by the subject along with a mobile phone around the neck to record videos continuously for ground truth.

Fig 5. TrigCam testing setup
Tigger timestamps for each trigger are dumped at the smartwatch and mobile phone and tabulated at the excel sheet. Video recorded by the TrigCam are timestamped.

Triggering Testing Results:
Following table shows the number of total number of hours tested (1st column), total number of triggers generated (2nd Column) during the testing and Triggers/hours ( 3rd column) for each type of triggering. Number of video recorded and the total hours of video recording at the TrigCam are tabulated in the 4th and 5th. 

Table1. Triggering summary

Smart Watch Sensor
Total Number of hours tested
Number of Triggers Generated
Triggers/hour
Number of Videos (20 seconds) recorded at RaspberryPi
Total Video Record Time
%Video recording time at TrigCam
%Video recording time saved at TrigCam
Orientation Triggering
7.72hrs
500
64.76
376
2.08hrs
27
73
Orientation and Magnetic Triggering
15.33hrs
956
62.36
509
2.82hrs
18.4
81.6
Accelerometer Triggering
3.83hrs
383
100
223
1.23hrs
32.2
67.8


Note- Above table shows the aggregate results over the week, result for each day are presented at the excel sheet. 

Percentage video recording saved is tabulated in the 6th column. 

%Video recording time saved= 1-(Total video record time at TrigCam/Total no. of hours TrigCam was tested)

Example:
 %Video recording time saved (Orientation Triggerig)=1-(2.08/7.72)=73


Note:
Orientation and Magnetic sensing generated less number of triggers / hour and is more efficient because it take magnetic proximity of the hand into consideration when detecting feeding type gestures, thus reducing false positive due to raising of the hand away from chest.
Orientation and Magnetic sensing showed highest amount of video recording time saved followed by Orientation triggering and Accel triggering.

      C. Power Consumption Testing Results 
i. TrigCam power consumption
TrigCam power consumption and number of hours lasting (Practical vs Theoretical)
Power consumption by the TrigCam was tested using a USB volt and ampere meter. 110mA is consumed during the idle periods (camera is OFF) by the TrigCam where as 310mA is consumed when camera is ON.


Fig. 6 Current consumption when Camera OFF(110mA) vs Current consumption when Camera ON(310mA)
% Current consumption reduced when Camera is OFF wrt Camera is ON = (Current consumption when Camera ON- Current consumption when camera OFF)/Current consumption when camera is ON.
=>(310-110)/310=64.51

Note- 64.51 % less current is consumed by the device when Camera is completely OFF.

For various triggering methods the camera is switched on for particular % of time 




Table2. %Power saving and number of hours lasted


Smart Watch Sensor
%Max power that can be saved when Camera is always OFF at TrigCam
%Video recording time saved
%Energy saving
Approx. number of hours observed before RaspberryPi is dead
Orientation Triggering
64.51
73
47.09
7hrs
Orientation and Magnetic Triggering
64.51
81.6
52.64
9hrs
Accelerometer Triggering
64.51
67.8
43.73
8hrs

ii. Smartwatch power consumption
SmartWatch power consumption and approx. number hours lasting observed for various triggering methods

Table3. SmartWatch power consumption and approx. number hours lasted before dead:


Smart Watch Sensor
Number of Sensor used
Current consumption at SmartWatch
Approx. Number of hours before smartwatch is dead
Orientation Triggering
3(Accel, Gyroscope, Magnetometer
0.4+5.9mA
6hrs
Orientation and Magnetic Triggering
2 (Accel, Magnetometer)
0.4mA+5mA
9hrs
Accelerometer Triggering
1( Accelerometer)
0.4mA
10hrs

III. CONCLUSION:
Power consumption of wearable systems is an important aspect of consideration. Camera based video recording forms an major power consuming component. Triggering of Camera can result in about 52% of energy saving is observed for Orientation and magnetic sensing followed by 47 % and 43% of energy saving for Orientation and Accelerometer based triggering techniques.
