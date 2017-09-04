package ro.pub.cs.controller;

import message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.pub.cs.communication.ClientConnection;
import ro.pub.cs.communication.ConnectionHandler;

import java.io.IOException;

@RestController
public class DataController {

    @Autowired
    public ConnectionHandler connectionHandler;

    @MessageMapping("/data")
    public void handle(Message data) {
        ClientConnection clientConnection = connectionHandler.getClientConnectionMap().get(data.getRequesterID());
        try {
            clientConnection.getObjectOutputStream().writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    @SendTo("/topic/message")
//    public OutputMessageDTO sendMessage(MessageDTO message) {
//        return new OutputMessageDTO(message, new Date());
//    }

}
