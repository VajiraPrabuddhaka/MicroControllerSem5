package com.anuradhawick.microcontroller.Communicator;

/**
 * Created by anuradhawick on 5/21/16.
 */
public class Message {
    public int motionDirection; // 1 forward, 0 idle, -1 for reverse
    public int tilt; // Out of 100
    public int tiltDirection; // -1 left, 0 none, 1 right
}
