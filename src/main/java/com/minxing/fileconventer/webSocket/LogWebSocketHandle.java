package com.minxing.fileconventer.webSocket;

import com.minxing.fileconventer.Handler.TailLogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.InputStream;

@ServerEndpoint("/log")
public class LogWebSocketHandle {
    private Logger log = LoggerFactory.getLogger(LogWebSocketHandle.class);
    private Process process;
    private InputStream inputStream;
    @Value("${log.url}")
    private String log_url;

    /**
     * 新的webSocket 请求开始
     */
    @OnOpen
    public void onOpen(Session session) {
            log.info("url:"+log_url);
        try {
            //执行tail -f  命令
            process = Runtime.getRuntime().exec("tail -f " + log_url);
            inputStream=process.getInputStream();
            //启动新的线程防止 输入流阻碍webSocket的线程   tail -f 命令会阻止当前线程
            TailLogHandler handler=new TailLogHandler(inputStream,session);
            handler.start();
        } catch (Exception e) {

        }
    }

    /**
     * webSocket 关闭
     */
    @OnClose
    public void onClose(){
        try {
              if (inputStream!=null)
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (process!=null){
            process.destroy();
        }
    }
    @OnError
    public void onError(Throwable throwable){
         throwable.printStackTrace();
    }
}
