package com.jason.videotunnelone;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NioPeriodChronicService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    NioBinder mBinder = new NioBinder(){
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initNio(View view){
        this.view = view;
        registerNioSelector();
        initWriteThread(); //prepare write thread
        listenNioSelector();
    }

    private Selector selector;
    private void registerNioSelector() {
        try {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();// 获得一个ServerSocket通道
            serverChannel.configureBlocking(false);// 设置通道为非阻塞
            this.selector = Selector.open();// 获得一个通道管理器
            serverChannel.socket().bind(new InetSocketAddress(8887));// 将该通道对应的ServerSocket绑定到port端口
            //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，
            //当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
        }
    }

    Thread sendThread;
    Handler sendHandle;
    private void initWriteThread() {
        sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                sendHandle = new Handler();
                Looper.loop();
            }
        });
        sendThread.start();
    }

    public void listenNioSelector() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //当注册的事件到达时，方法返回；否则,该方法会一直阻塞
                    try {
                        selector.select();
                        Iterator iterator = NioPeriodChronicService.this.selector.selectedKeys().iterator();// 获得selector中选中的项的迭代器，选中的项为注册的事件
                        while (iterator.hasNext()) {
                            SelectionKey selectionKey = (SelectionKey) iterator.next();
                            iterator.remove();// 删除已选的key,以防重复处理
                            if (selectionKey.isAcceptable()) { // 客户端请求连接事件
                                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                                SocketChannel socketChannel = serverSocketChannel.accept(); // 获得和客户端连接的通道
                                socketChannel.configureBlocking(false); // 设置成非阻塞
                                socketChannel.register(NioPeriodChronicService.this.selector, SelectionKey.OP_READ); //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
                            } else if (selectionKey.isReadable()) { // 获得了可读的事件
                                nIOHandleReadChannel(selectionKey,(SocketChannel) selectionKey.channel());
                            }
                        }
                    } catch (IOException e) {
                        if (e.getMessage() != null && e.getMessage().contains("closed")
                                && e.getMessage().contains("Broken")) {
                        }
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    Map<String,SocketChannel> socketChannels = new HashMap<>();
    Map<String,SelectionKey> socketChannelKeys = new HashMap<>();
    Map<SelectionKey,String> socketChannelKeyUsers = new HashMap<>();
    public void nIOHandleReadChannel(SelectionKey selectionKey,SocketChannel socketChannel) {
        try {
            String user = socketChannelKeyUsers.get(selectionKey);
            if ( user == null) {
                user=verifyNioChannelUser(socketChannel);
                if (user==null||user.equals("")) {
                    if (view != null) view.showError("验证用户失败");
                    return;
                }
                socketChannels.put(user, socketChannel);
                socketChannelKeys.put(user, selectionKey);
                socketChannelKeyUsers.put(selectionKey, user);
                showConnectedUsers();
            } else {
                handleNIoChannelDataRead(selectionKey,socketChannel);
            }
        } catch (Exception e) {
            //ignore
        } finally {
            //关闭socket
        }
    }
    private String verifyNioChannelUser(SocketChannel socketChannel) {
        String user = null;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            int readedCount = socketChannel.read(buffer);
//            user = reader.readLine();
            byte[] tempBytes = buffer.array();
            byte[] targetBytes = new byte[readedCount];
            for (int i = 0; i < readedCount; i++) {
                targetBytes[i] =tempBytes[i];
            }
            user = new String(targetBytes, "UTF-8");
            user = user.replace("\n", "");
            user = user.replace("\r", "");
            socketChannel.write(ByteBuffer.wrap("success\n\r".getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    private synchronized void handleNIoChannelDataRead(final SelectionKey selectionKey, final SocketChannel socketChannel) {
        int contentLength = 0;
        for (; ; ) {
            try {
                if (contentLength == 0) {
                    ByteBuffer buffer = ByteBuffer.allocate(4);
                    long readedByteCount = socketChannel.read(buffer);
                    if (readedByteCount == -1) { //客户端关闭了连接(=0未判断)
                        disconnectNioUser(socketChannelKeyUsers.get(selectionKey));
                        break;
                    } else {
                        contentLength = Tool.byte4ToInt(buffer.array(), 0);
                    }
                } else {
                    ByteBuffer buffer = ByteBuffer.allocate(contentLength);
                    int readedByteCount = socketChannel.read(buffer);
                    if (readedByteCount != -1) {
                        String ss = new String(buffer.array(), "UTF-8");
                        if (view != null) view.showData(socketChannelKeyUsers.get(selectionKey), ss);
                    }
                    contentLength = 0;
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void disconnectNioUser(final String disconnectUserName) {
        SocketChannel socketChannel = socketChannels.get(disconnectUserName);
        if(socketChannel==null) {
            SelectionKey selectionKey = socketChannelKeys.get(disconnectUserName);
            if (selectionKey != null) {
                if (socketChannelKeyUsers.get(selectionKey) != null) {
                    socketChannelKeyUsers.remove(selectionKey);
                }
                socketChannelKeys.remove(selectionKey);
                selectionKey.cancel();
            }
            showConnectedUsers();
            return;
        }
        if (!(socketChannel.isConnected())){
            socketChannels.remove(disconnectUserName);
            SelectionKey selectionKey = socketChannelKeys.get(disconnectUserName);
            if (selectionKey != null) {
                socketChannelKeys.remove(disconnectUserName);
                socketChannelKeyUsers.remove(selectionKey);
            }
            try {
                if (selectionKey != null) {
                    selectionKey.cancel();
                }
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            showConnectedUsers();
            return;
        }
        socketChannels.remove(disconnectUserName);
        SelectionKey selectionKey = socketChannelKeys.get(disconnectUserName);
        if (selectionKey != null) {
            socketChannelKeys.remove(disconnectUserName);
            socketChannelKeyUsers.remove(selectionKey);
        }
        try {
            if (selectionKey != null) {
                selectionKey.cancel();
            }
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        showConnectedUsers();
    }


    private void nioWriteString(final String sendUser,final String sendMsg) {

        sendHandle.post(new Runnable() {
            @Override
            public void run() {
                nioWriteStringImp(sendUser,sendMsg);
            }
        });

    }
    private synchronized void nioWriteStringImp(String sendUser,String sendMsg) {
        SocketChannel socketChannel = socketChannels.get(sendUser);
        if (socketChannel == null) {
            if (view != null) view.showError("未找到连接的用户："+sendUser);
            return;
        }
        if (!socketChannel.isConnected()) {
            disconnectNioUser(sendUser);
            if (view != null) view.showError("用户连接已断开："+sendUser);
            return;
        }

        try {
            byte[] tempContentBytes = sendMsg.getBytes(("UTF-8"));
            int contentLength = tempContentBytes.length;
            byte[] contentLengthBytes = Tool.intToByte4(contentLength);
            byte[] totalBytes = Tool.combineBytes(new byte[4 + contentLength], contentLengthBytes, tempContentBytes);
            ByteBuffer byteBuffer = ByteBuffer.wrap(totalBytes);
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            if (view != null) view.showError("发送失败：" + e.getCause() + e.getMessage());
            e.printStackTrace();
        }
    }

        private void showConnectedUsers() {

        String temp ="";
        for (String s : socketChannels.keySet()) {
            temp += (s+"\n\r");
        }
        if (view != null) {
            view.showConnectedUsers(temp);
        }
    }



    public class NioBinder extends Binder{
        public void initNio(View view){
            NioPeriodChronicService.this.initNio(view);
        }
        public void disconnectNioUser(final String disconnectUserName) {
            NioPeriodChronicService.this.disconnectNioUser(disconnectUserName);
        }
        public void nioWriteString(String sendUser,String sendMsg) {
            NioPeriodChronicService.this.nioWriteString(sendUser,sendMsg);
        }
        public void setView(View view) {
            NioPeriodChronicService.this.setView(view);
        }
    }

    private void setView(View view) {
        this.view = view;
    }


    View view;
    public interface View{
        void showError(String ss);
        void showConnectedUsers(String userNames);
        void showData(final String user, final String ss);
    }


}
