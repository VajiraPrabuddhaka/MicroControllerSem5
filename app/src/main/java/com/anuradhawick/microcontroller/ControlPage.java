package com.anuradhawick.microcontroller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ControlPage extends AppCompatActivity implements SensorEventListener {
    private ProgressBar rightTurn, leftTurn;
    private ImageView wheel;
    private SensorManager sensorManager;
    private float[] mRotationMatrix = new float[16];
    private float[] orientationVals = new float[4];
    private TextView tv, tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_page);
        rightTurn = (ProgressBar) findViewById(R.id.rightTurn);
        leftTurn = (ProgressBar) findViewById(R.id.leftTurn);
        wheel = (ImageView) findViewById(R.id.wheel);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tv = (TextView) findViewById(R.id.textView);
        tv1 = (TextView) findViewById(R.id.textView2);
        tv2 = (TextView) findViewById(R.id.textView3);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), sensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(this, "Success", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // It is good practice to check that we received the proper sensor event
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // Convert the rotation-vector to a 4x4 matrix.
            SensorManager.getRotationMatrixFromVector(mRotationMatrix,
                    event.values);
//            SensorManager
//                    .remapCoordinateSystem(mRotationMatrix,
//                            SensorManager.AXIS_X, SensorManager.AXIS_Z,
//                            mRotationMatrix);
            SensorManager.getOrientation(mRotationMatrix, orientationVals);

            // Optionally convert the result from radians to degrees
            orientationVals[0] = (float) Math.toDegrees(orientationVals[0]);
            orientationVals[1] = (float) Math.toDegrees(orientationVals[1]);
            orientationVals[2] = (float) Math.toDegrees(orientationVals[2]);

            // value set 1 +90 to -90
//            System.out.println(orientationVals[0] + " " + orientationVals[1] + " " + orientationVals[2]);
            tv.setText("" + Math.round(orientationVals[2] / 45 * 100));
            rightTurn.setProgress(-1 * (Math.round(orientationVals[1] / 45 * 100)) - 15);
            leftTurn.setProgress(Math.round(orientationVals[1] / 45 * 100) - 15);
            if (-1 * (Math.round(orientationVals[1] / 45 * 100)) - 15 > 0 || Math.round(orientationVals[1] / 45 * 100) - 15 > 0) {
                wheel.setRotation(-1 * orientationVals[1] / 45 * 100);
            } else {
                wheel.setRotation(0f);
            }
            tv1.setText("" + Math.round(orientationVals[0] / 45 * 100));
            tv2.setText("" + Math.round(orientationVals[1] / 45 * 100));

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
