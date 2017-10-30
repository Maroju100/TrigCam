#include <iostream>
#include "APDS9960_RPi.h"

using namespace std;

// Global Variables
APDS9960_RPi apds = APDS9960_RPi();
uint8_t proximity_data = 0;

int main() {

	// Initialize Serial port
	cout << endl;
	cout << "------------------------------------" << endl;
	cout << "SparkFun APDS-9960 - ProximitySensor" << endl;
	cout << "------------------------------------" << endl;

	// Initialize APDS-9960 (configure I2C and initial values)
	if ( apds.init() ) {
		cout << "APDS-9960 initialization complete" << endl;
	} else {
		cout << "Something went wrong during APDS-9960 init!" << endl;
	}

	// Adjust the Proximity sensor gain
	if ( !apds.setProximityGain(PGAIN_2X) ) {
		cout << "Something went wrong trying to set PGAIN" << endl;
	}

	// Start running the APDS-9960 proximity sensor (no interrupts)
	if ( apds.enableProximitySensor(false) ) {
		cout << "Proximity sensor is now running" << endl;
	} else {
		cout << "Something went wrong during sensor init!" << endl;
	}

	while(1) {

		// Read the proximity value
		if ( !apds.readProximity(proximity_data) ) {
			cout << "Error reading proximity value" << endl;
		} else {
			cout << "Proximity: ";
			cout << int(proximity_data) << endl;
		}

		// Wait 250 ms before next reading
		delay(250);
	}
	return 0;
}