package com.anuradhawick.microcontroller.Communicator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by anuradhawick on 5/21/16.
 */
public class SocketConnector {
    private ServerSocket serverSocket;
    private Socket socket;

    public void startListening() {
        try {
            serverSocket = new ServerSocket(20000);

            while (true) {
                socket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String line = "";
                while ((line = br.readLine()) != null) {
                    // line is the read string
                    bw.write(""); // what ever the output
                    bw.flush();
                }

                br.close();
                bw.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
