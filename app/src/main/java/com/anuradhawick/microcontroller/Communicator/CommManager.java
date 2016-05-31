package com.anuradhawick.microcontroller.Communicator;

import com.anuradhawick.microcontroller.SpeedCalculator.Calculator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLOutput;

/**
 * Created by anuradhawick on 5/24/16.
 */
public class CommManager {
    private CommStatus status = CommStatus.UN_INITIALIZED;
    private Socket clientSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private ActiveThread activeThread;
    private Calculator calculator;

    public void startClient(String ip, int port, Calculator calculator) {
        StartClient startClient = new StartClient(ip, port);
        startClient.start();
        this.calculator = calculator;
        activeThread = new ActiveThread();
    }

    public CommStatus getStatus() {
        return status;
    }

    private class StartClient extends Thread {
        private int port;
        private String ip;

        public StartClient(String ip, int port) {
            this.port = port;
            this.ip = ip;
        }

        @Override
        public void run() {
            try {
                status = CommStatus.CONNECTING;
                clientSocket = new Socket(this.ip, this.port);
                dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                dataInputStream = new DataInputStream(clientSocket.getInputStream());
                status = CommStatus.CONNECTED;
                activeThread.start();
            } catch (IOException e) {
                status = CommStatus.FAILED;
                e.printStackTrace();
            }
        }
    }

    private class ActiveThread extends Thread {

        public void sendMessage(String message) {
            try {
//                dataOutputStream.write((message + "\r\n").getBytes());
                dataOutputStream.write((message + "\r").getBytes());
                dataOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) try {
                byte[] temp = new byte[10];
//                String ss = calculator.getMessage();
//                sendMessage(ss);
//                System.out.print(calculator.getMessage() +" _ ");
                System.out.println("Sending");
                dataOutputStream.write((calculator.getMessage().trim() + "\r").getBytes());
                System.out.println("Receiving");
                dataInputStream.read(temp, 0, 10);
                String s = new String(temp);
                System.out.println("Looping");
                System.out.println("Received: "+s);
//                System.out.println(dataInputStream.read());
                sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
