package com.anuradhawick.microcontroller.SpeedCalculator;

import com.anuradhawick.microcontroller.Communicator.CommManager;

/**
 * forward - 1, back - 2 and still - 0
 * inclination - out of 255
 * turn right -1, left - 2
 * speed - out of 255
 * forward {left/right}direction - forward 1 backward 2
 * Created by anuradhawick on 5/26/16.
 */
public class Calculator {
    private int leftDirection = 1;
    private int rightDirection = 1;
    private int leftSpeed = 0;
    private int rightSpeed = 0;
    private static long time = System.currentTimeMillis();

    public void updateSpeed(int vehicleDirection, int speed, int inclination, int turn) {
        if (speed == 0) {
            leftSpeed = rightSpeed = 0;
            // rotate at the same point
            if (inclination == 0) {
                // send message and return
                return;
            }
            switch (turn) {
                case 1:
                    leftSpeed = Math.min(inclination, 400);
                    leftDirection = 1;
                    break;
                case 2:
                    rightSpeed = Math.min(inclination, 400);
                    rightDirection = 1;
                    break;
                default:
                    break;
            }
            return;
        }
        switch (vehicleDirection) {
            case 1:
                // Use inclination to calculate the speeds
                leftSpeed = rightSpeed = speed;
                leftDirection = rightDirection = 1;
                // scaling the inclination to match the speed
                if (inclination == 0) {
                    // send message and return
                    return;
                }
                inclination = (int) ((float) inclination / 999.0 * (float) speed);
                switch (turn) {
                    case 1:
                        // Turn right
                        leftSpeed = Math.min(speed + inclination, 999);
                        rightSpeed = Math.max(speed - inclination, 400);
                        break;
                    case 2:
                        leftSpeed = Math.max(speed - inclination, 400);
                        rightSpeed = Math.min(speed + inclination, 999);
                        // Turn left
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                // Use inclination to calculate the speeds
                leftSpeed = rightSpeed = speed;
                leftDirection = rightDirection = 2;
                if (inclination == 0) {
                    // send message and return
                    return;
                }
                inclination = (int) ((float) inclination / 999.0 * (float) speed);
                switch (turn) {
                    case 1:
                        // Turn right
                        leftSpeed = Math.min(speed + inclination, 999);
                        rightSpeed = Math.max(speed - inclination, 400);
                        break;
                    case 2:
                        leftSpeed = Math.max(speed - inclination, 400);
                        rightSpeed = Math.min(speed + inclination, 999);
                        // Turn left
                        break;
                    default:
                        break;
                }
                break;
            case 0:
                leftSpeed = rightSpeed = 0;
                break;
        }
    }


    public String getMessage() {
        if (time + 100 < System.currentTimeMillis()) {
            time = System.currentTimeMillis();
            if (leftDirection == rightDirection && rightDirection == 1) {
                return "1" + String.format("%03d", leftSpeed) + String.format("%03d", rightSpeed);
            } else if (leftDirection == rightDirection && rightDirection == 2) {
                return "2" + String.format("%03d", leftSpeed) + String.format("%03d", rightSpeed);
            } else if (rightDirection == 2) {
                return "3" + String.format("%03d", leftSpeed) + String.format("%03d", rightSpeed);
            } else {
                return "4" + String.format("%03d", leftSpeed) + String.format("%03d", rightSpeed);
            }
        } else {
            return "1000000";
        }
    }
}
