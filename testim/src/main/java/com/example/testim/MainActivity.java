package com.example.testim;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements Runnable, View.OnClickListener {
  /**
   * Android Tcp即时通讯客户端
   */
    private TextView tv_msg = null;
    private EditText ed_msg = null;
    private Button btn_send = null;
    private static final String HOST = "10.10.11.147";//服务器地址
    private static final int PORT = 8099;//连接端口号
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;

    //接收线程发送过来信息，并用TextView追加显示
    public Handler mHandler = new Handler() {
      public void handleMessage(Message msg) {
        super.handleMessage(msg);
        tv_msg.append((CharSequence) msg.obj);
      }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      tv_msg = (TextView) findViewById(R.id.textView);
      ed_msg = (EditText) findViewById(R.id.editText);
      btn_send = (Button) findViewById(R.id.button);
      btn_send.setOnClickListener(this);
      //启动线程，连接服务器，并用死循环守候，接收服务器发送过来的数据
      new Thread(this).start();
    }

  @Override
  public void onClick(View view) {
    final String msg = ed_msg.getText().toString();
    if (socket.isConnected()) {//如果服务器连接
      if (!socket.isOutputShutdown()) {//如果输出流没有断开
        new Thread(new Runnable() {
          @Override
          public void run() {
            out.println(msg/*+"\n\r\n\r\n\r\n\r\n\r"*/);//点击按钮发送消息

          }
        })
        .start();


        ed_msg.setText("");//清空编辑框
      }
    }
  }


    /**
     * 读取服务器发来的信息，并通过Handler发给UI线程
     */
    @Override
    public void run() {
      // 连接到服务器
      try {
        Log.e("LLL","连接服务器1");
        socket = new Socket(HOST, PORT);//连接服务器
        Log.e("LLL","连接服务器2");
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//接收消息的流对象
        Log.e("LLL","连接服务器3");
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);//发送消息的流对象
        Log.e("LLL","连接服务器4");
      } catch (IOException ex) {
        ex.printStackTrace();
        Log.e("LLL","连接服务器失败：" + ex.getMessage());
      }


      try {

        while (true) {//死循环守护，监控服务器发来的消息
          if (!socket.isClosed()/*如果服务器链接没有关闭*/&&socket.isConnected()/*连接正常*/&&!socket.isInputShutdown()/*如果输入流没有断开*/) {
                String getLine;
                if ((getLine = in.readLine()) != null) {//读取接收的信息
                  getLine += "\n";
                  Message message=new Message();
                  message.obj=getLine;
                  mHandler.sendMessage(message);//通知UI更新
                } else {

                }
          }

        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }



  public void ShowDialog(String msg) {
    new AlertDialog.Builder(this).setTitle("通知").setMessage(msg)
            .setPositiveButton("ok", new DialogInterface.OnClickListener() {

              @Override
              public void onClick(DialogInterface dialog, int which) {

              }
            }).show();
  }


}
