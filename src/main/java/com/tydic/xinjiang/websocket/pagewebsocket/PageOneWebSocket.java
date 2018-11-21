package com.tydic.xinjiang.websocket.pagewebsocket;

import com.tydic.xinjiang.common.enumconstant.ServerEndpointValue;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = ServerEndpointValue.COM_TYDIC_XINJIANG_WEBSOCKET)
@Component  //将这个监听器纳入到Spring容器中进行管理
public class PageOneWebSocket {

    //静态变量，用来记录当前在线连接数
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    //concurrent包的线程安全Set，用来存放同一个页面对应的WebSocket对象集
    private static CopyOnWriteArraySet<PageOneWebSocket> pageOneWebSocketSet = new CopyOnWriteArraySet<PageOneWebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        pageOneWebSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前访问" + ServerEndpointValue.COM_TYDIC_XINJIANG_WEBSOCKET + "的人数为" + getOnlineCount());
        try {
            sendMessage("Hello world");
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        pageOneWebSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前访问" + ServerEndpointValue.COM_TYDIC_XINJIANG_WEBSOCKET + "的人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);

        //群发消息
        for (PageOneWebSocket item : pageOneWebSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 群发自定义消息，推送消息到页面
     */
    public static void sendInfo(String message) throws IOException {
        for (PageOneWebSocket item : pageOneWebSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized AtomicInteger getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        PageOneWebSocket.onlineCount.getAndIncrement();
    }

    public static synchronized void subOnlineCount() {
        PageOneWebSocket.onlineCount.getAndDecrement();
    }
}
