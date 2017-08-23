package ro.pub.cs.communication;

import message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;

import java.io.*;
import java.util.Base64;

/**
 */
public class PictureWorkerRunnable implements Runnable {
    private ObjectInputStream input;
    private Message data;

    public PictureWorkerRunnable(ObjectInputStream input) {
        this.input = input;
    }

    private SimpMessagingTemplate template;

    @Override
    public void run() {
        System.out.println("Starting the picture reader");

        //template.convertAndSend("/topic/message", "Hello from server!");

        while (true) {
            try {
                data = (Message) input.readObject();
                readData();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readData() {
        final long time = System.currentTimeMillis();
        if (data.getType() == Message.PICTURE_FILE) {
            byte[] pictureData = data.getPicture();


            template.convertAndSend("/topic/frame", new TextMessage(Base64.getEncoder().encode(pictureData)));//new BinaryMessage(pictureData));


            if (pictureData.length != 0) {
                OutputStream fileOutputStream = null;
                String fileName = "src/main/resources/img/img" + time + ".jpg";
                try {
                    fileOutputStream = new BufferedOutputStream(new FileOutputStream(fileName));
                    fileOutputStream.write(pictureData);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("Request processed: " + time);
            }
        }
    }

    public SimpMessagingTemplate getTemplate() {
        return template;
    }

    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }
}