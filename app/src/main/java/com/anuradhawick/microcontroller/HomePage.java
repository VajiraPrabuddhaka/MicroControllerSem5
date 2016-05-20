package com.anuradhawick.microcontroller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    private EditText ipAddress;
    private EditText portNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button proceedButton = (Button) findViewById(R.id.proceedButton);
        portNumber = (EditText) findViewById(R.id.portNumber);
        ipAddress = (EditText) findViewById(R.id.ipAddress);
        proceedButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int port;
        String ip;
        try {
            URI uri = new URI("my://" + ipAddress.getText() + ":" + portNumber.getText());
            port = uri.getPort();
            ip = uri.getHost();
            if (ip == null || port == -1) {
                throw new URISyntaxException("Error ", "Error");
            }
            Toast.makeText(this, "IP: " + ip + " port " + port, Toast.LENGTH_SHORT).show();
        } catch (URISyntaxException ex) {
            Toast.makeText(this, "Please enter a valid IP and a Port number", Toast.LENGTH_SHORT).show();
        }
        Intent i = new Intent(this, ControlPage.class);
        startActivity(i);
    }

}
