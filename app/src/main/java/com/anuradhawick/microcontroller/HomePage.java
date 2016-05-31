package com.anuradhawick.microcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anuradhawick.microcontroller.Communicator.CommManager;
import com.anuradhawick.microcontroller.Communicator.CommStatus;
import com.anuradhawick.microcontroller.SpeedCalculator.Calculator;

import java.net.URI;
import java.net.URISyntaxException;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    private EditText ipAddress;
    private EditText portNumber;
    private CommManager cm;
    public static Calculator calculator = new Calculator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button proceedButton = (Button) findViewById(R.id.proceedButton);
        portNumber = (EditText) findViewById(R.id.portNumber);
        ipAddress = (EditText) findViewById(R.id.ipAddress);
        ipAddress.setText("192.168.137.198");
        portNumber.setText("7500");
        proceedButton.setOnClickListener(this);
        calculator.updateSpeed(1, 0, 0, 1);
    }

    @Override
    public void onClick(View v) {
        final int port;
        final String ip;
        try {
            URI uri = new URI("my://" + ipAddress.getText() + ":" + portNumber.getText());
            port = uri.getPort();
            ip = uri.getHost();
            if (ip == null || port == -1) {
                throw new URISyntaxException("Error ", "Error decoding the IP address and port");
            }
            cm = new CommManager();
            cm.startClient(ip, port, calculator);
            start = System.currentTimeMillis();
            while (cm.getStatus() != CommStatus.CONNECTED) {
                if (timeOut()) {
                    Toast.makeText(this, "Connection failed, please retry!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Toast.makeText(this, "Connection success!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, ControlPage.class);
            startActivity(i);
        } catch (URISyntaxException ex) {
            Toast.makeText(this, "Please enter a valid IP and a Port number!", Toast.LENGTH_SHORT).show();
        }
    }


    private static long start;

    private boolean timeOut() {
        if (start + 5000 < System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

}
