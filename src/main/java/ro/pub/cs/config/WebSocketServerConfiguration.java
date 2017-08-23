//package ro.pub.cs.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.BinaryMessage;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//import org.springframework.web.socket.handler.BinaryWebSocketHandler;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//
//@Configuration
//@EnableWebSocket
//public class WebSocketServerConfiguration implements WebSocketConfigurer {
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(new MyTextHandler(), "/text");
//        registry.addHandler(new MyBinaryHandler(), "/topic/images");
//    }
//
//    @Component
//    public static class MyTextHandler extends TextWebSocketHandler {
//        public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
////            session.sendMessage(new TextMessage("hello world"));
//        }
//    }
//
//    @Component
//    public static class MyBinaryHandler extends BinaryWebSocketHandler {
//        public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
////            try {
////                session.sendMessage(new BinaryMessage("hello world".getBytes()));
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//        }
//    }
//}
