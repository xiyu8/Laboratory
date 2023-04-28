package com.jason.videotunneltwo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class NioPeriodChronicActivity extends AppCompatActivity implements NioPeriodChronicService.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_peroid_chronic);

        send_msg_content = findViewById(R.id.send_msg_content);
        receive_msg_content = findViewById(R.id.receive_msg_content);
        server_ip = findViewById(R.id.server_ip);
        server_port = findViewById(R.id.server_port);
        user_name = findViewById(R.id.user_name);
        connection_status = findViewById(R.id.connection_status);

        server_ip.setText("192.168.137.36");
        server_port.setText("8887");
        user_name.setText("user1");



        startService(new Intent(this, NioPeriodChronicService.class));

        bindService(new Intent(this, NioPeriodChronicService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                nioBinder = (NioPeriodChronicService.NioBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    String port = null;
    String host = null;
    String userName = null;

    EditText send_msg_content;
    EditText server_ip;
    EditText server_port;
    TextView receive_msg_content;
    TextView user_name;
    TextView connection_status;
    NioPeriodChronicService.NioBinder nioBinder;
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connect_server:
                if (host != null && !host.equals("")) {
                    showError("当前已有连接");
                    return;
                }
                port = server_port.getText().toString();
                host = server_ip.getText().toString();
                userName = user_name.getText().toString();
                if (port == null || host == null || userName == null) {
                    return;
                }
                host.replace(" ", "");
                port.replace(" ", "");
                userName.replace(" ", "");
                if (port.equals("") || host.equals("") || userName.equals("")) {
                    return;
                }


                nioBinder.registerNIoSelector(this);
                nioBinder.initWriteThread();
                nioBinder.nioConnect(host,port,userName);

                break;
            case R.id.send_msg:
                final String sendMsg = send_msg_content.getText().toString();
                if (sendMsg == null || sendMsg.equals("")) {
                    return;
                }
                nioBinder.nioWriteString(sendMsg);
                send_msg_content.setText("");
                break;
            case R.id.disconnect_server:
                nioBinder.nioDisconnect();
                break;

        }
    }




    public void showData(final String ss) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                receive_msg_content.append(ss + "\n\r");
            }
        });
    }

    public void showConnection(final String ip, final String port, final String user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ip == null) {
                    connection_status.setText("unconnect");
                } else {
                    connection_status.setText("ip:" + ip + "port:" + port + "user:" + user);
                }
            }
        });
    }

    public void showError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NioPeriodChronicActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}