package com.anuradhawick.microcontroller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anuradhawick.microcontroller.SpeedCalculator.Calculator;

public class ControlPage extends AppCompatActivity implements SensorEventListener, View.OnTouchListener {
    private ProgressBar rightTurn, leftTurn;
    private ImageView wheel, brake, gas;
    private SensorManager sensorManager;
    private float[] mRotationMatrix = new float[16];
    private float[] orientationVals = new float[4];
    private SeekBar speedSelector;

    // Static variables
    // Keeping track of the pressed item
    private static long gasPress = System.currentTimeMillis();
    private static long brakePress = System.currentTimeMillis();
    // Speeds
    private int inclination = 0, direction = 1, speed = 0, turn = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_page);
        rightTurn = (ProgressBar) findViewById(R.id.rightTurn);
        leftTurn = (ProgressBar) findViewById(R.id.leftTurn);
        wheel = (ImageView) findViewById(R.id.wheel);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gas = (ImageView) findViewById(R.id.gas);
        brake = (ImageView) findViewById(R.id.brake);
        speedSelector = (SeekBar) findViewById(R.id.speedSelector);
        gas.setOnTouchListener(this);
        brake.setOnTouchListener(this);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), sensorManager.SENSOR_DELAY_UI);
            Toast.makeText(this, "Success", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    HomePage.calculator.updateSpeed(direction, speed, inclination, turn);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        //
                    }
                }
            }
        }).start();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int leftTurnValue = 0, rightTurnValue = 0;
        // It is good practice to check that we received the proper sensor event
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // Convert the rotation-vector to a 4x4 matrix.
            SensorManager.getRotationMatrixFromVector(mRotationMatrix,
                    event.values);
            SensorManager.getOrientation(mRotationMatrix, orientationVals);

            // Optionally convert the result from radians to degrees
            orientationVals[0] = (float) Math.toDegrees(orientationVals[0]);
            orientationVals[1] = (float) Math.toDegrees(orientationVals[1]);
            orientationVals[2] = (float) Math.toDegrees(orientationVals[2]);

            rightTurn.setProgress(-1 * (Math.round(orientationVals[1] / 45 * 100)) - 15);
            leftTurn.setProgress(Math.round(orientationVals[1] / 45 * 100) - 15);
            if (-1 * (Math.round(orientationVals[1] / 45 * 100)) - 15 > 0 || Math.round(orientationVals[1] / 45 * 100) - 15 > 0) {
                wheel.setRotation(-1 * orientationVals[1] / 45 * 100);
                leftTurnValue = (Math.round(orientationVals[1] / 45 * 100) - 15);
                rightTurnValue = ((-1) * (Math.round(orientationVals[1] / 45 * 100)) - 15);
                if (leftTurnValue > 0) {
                    leftTurnValue = Math.min(leftTurnValue, 999);
                    leftTurnValue = (int) ((float) leftTurnValue / 100.0 * (999));
                    rightTurnValue = 0;
                    // Turn left
                    turn = 2;
                    inclination = leftTurnValue;
                } else if (rightTurnValue > 0) {
                    rightTurnValue = Math.min(rightTurnValue, 999);
                    rightTurnValue = (int) ((float) rightTurnValue / 100.0 * (999));
                    leftTurnValue = 0;
                    // Right turn
                    turn = 1;
                    inclination = rightTurnValue;
                }
            } else {
                wheel.setRotation(0f);
                inclination = 0;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.gas) {
            gasPress = System.currentTimeMillis();
            direction = 1;
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                speed = speedSelector.getProgress();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                inclination = 0;
                direction = 1;
                speed = 0;
                turn = 1;
            }
        }
        if (v.getId() == R.id.brake) {
            brakePress = System.currentTimeMillis();
            if (brakePress > gasPress + 100) {
                direction = 2;
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    speed = speedSelector.getProgress();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    inclination = 0;
                    direction = 1;
                    speed = 0;
                    turn = 1;
                }
            }
        }
        return true;
    }


}
