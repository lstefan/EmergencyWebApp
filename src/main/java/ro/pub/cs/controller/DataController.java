package ro.pub.cs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.pub.cs.dto.MessageDTO;
import ro.pub.cs.dto.OutputMessageDTO;

import java.util.Date;

@RestController
public class DataController {

    @MessageMapping("/data")
    @SendTo("/topic/message")
    public OutputMessageDTO sendMessage(MessageDTO message) {
        return new OutputMessageDTO(message, new Date());
    }

}
