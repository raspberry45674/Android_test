package com.example.client;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.lang.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;

import androidx.appcompat.app.AppCompatActivity;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    final String SERVER_IP = "192.168.0.201";
    final int PORT = 18765;
    public Button connectBtn;
    public Button goBtn;
    public static TextView datatext;
    public static TextView recvCount;

    public static InputStream dataInput;
    public static OutputStream dataOutput;
    public static Socket socket;

    public Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectBtn = findViewById(R.id.btn01);
        datatext = findViewById(R.id.recvText);
        recvCount = findViewById(R.id.recvByte);
        toast = Toast.makeText(this, "READY", Toast.LENGTH_SHORT);
        toast.show();
        toast = Toast.makeText(this, "수신 시작", Toast.LENGTH_SHORT);
        recvCount.setText("check");
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket = new Socket();
                            socket.connect(new InetSocketAddress(SERVER_IP, PORT));
                            try {
                                connectBtn.setEnabled(false);
                                connectBtn.setVisibility(View.GONE);
                                recvCount.setText("cwh");
                            } catch (Exception e1) {
                                Log.d("btn err", "버튼에러");
                            }
                            start2();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("ClientThread", "실패");
                        }
                    }
                }).start();
            }
        });
    }
    public static void start2() throws IOException {
        try {
            int maxBufferSize = 3080;
            byte[] recvBuffer= new byte[maxBufferSize];;
            dataInput = socket.getInputStream();
            dataOutput = socket.getOutputStream();
            byte goSever = 1;
            dataOutput.write(goSever);
            datatext.setText("|   degree|   humidity|  intensity|       gas|      dust|     sound|" + "\n");
            for (int i = 0; i < 21; i++) {
                dataInput.read(recvBuffer);
                String message = new String(recvBuffer);

                datatext.append(message);
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
        socket.close();
        System.out.println("start 소켓 닫음");
    }

    @Override
    protected void onStop() {  //앱 종료시
        super.onStop();
        try {
            socket.close(); //소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}