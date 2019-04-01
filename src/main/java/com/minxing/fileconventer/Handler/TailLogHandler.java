package com.minxing.fileconventer.Handler;

import javax.websocket.Session;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TailLogHandler extends Thread {
    private BufferedReader reader;
    private Session session;

    public TailLogHandler(InputStream in, Session session) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.session = session;
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line=reader.readLine())!=null){
                //将实时日志通过webSocket发送给客户端 给每一行添加一个html 换行
                session.getBasicRemote().sendText(line+"<br>");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
