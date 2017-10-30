#include <iostream>
#include "APDS9960_RPi.h"

using namespace std;

// Global Variables
APDS9960_RPi apds = APDS9960_RPi();
uint16_t ambient_light = 0;
uint16_t red_light = 0;
uint16_t green_light = 0;
uint16_t blue_light = 0;

int main() {

	// Initialize Serial port
	cout << endl;
	cout << "------------------------------------" << endl;
	cout << "  SparkFun APDS-9960 - ColorSensor  " << endl;
	cout << "------------------------------------" << endl;

	// Initialize APDS-9960 (configure I2C and initial values)
	if ( apds.init() ) {
		cout << "APDS-9960 initialization complete" << endl;
	} else {
		cout << "Something went wrong during APDS-9960 init!" << endl;
	}

	// Start running the APDS-9960 light sensor (no interrupts)
	if ( apds.enableLightSensor(false) ) {
		cout << "Light sensor is now running" << endl;
	} else {
		cout << "Something went wrong during sensor init!" << endl;
	}

	// Wait for initialization and calibration to finish
	delay(500);

	while(1) {
		// Read the light levels (ambient, red, green, blue)
		if (  !apds.readAmbientLight(ambient_light) ||
				!apds.readRedLight(red_light) ||
				!apds.readGreenLight(green_light) ||
				!apds.readBlueLight(blue_light) ) {
			cout << "Error reading light values" << endl;
		}
		else {
			cout << "Ambient: ";
			cout << int(ambient_light);
			cout << " Red: ";
			cout << int(red_light);
			cout << " Green: ";
			cout << int(green_light);
			cout << " Blue: ";
			cout << int(blue_light) << endl;
		}

		// Wait 1 second before next reading
		delay(1000);
	}
	return 0;
}