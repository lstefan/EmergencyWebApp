package ro.pub.cs.communication;

import message.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class MessageWriter {
    private ObjectOutputStream output;
    private SimpMessagingTemplate template;

    public MessageWriter(ObjectOutputStream objectOutputStream) {
        this.output = objectOutputStream;
    }

    public void writeMessage(Message message) {
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SimpMessagingTemplate getTemplate() {
        return template;
    }

    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }
}
