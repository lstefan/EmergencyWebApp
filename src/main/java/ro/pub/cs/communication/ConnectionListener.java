package ro.pub.cs.communication;

import message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionListener implements Runnable {

    public static final int TCP_PORT = 9999;
    public static final int UDP_PORT = 9998;

    static ArrayList<Thread> clientThreads;

    private SimpMessagingTemplate template;

    @Override
    public void run() {
        System.out.println("Starting the connection listener");

        clientThreads = new ArrayList<>();

        ServerSocket serverTCP = null;
        DatagramSocket serverUDP = null;

        try {
            serverTCP = new ServerSocket(TCP_PORT);
            serverUDP = new DatagramSocket(UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectOutputStream out;
        ObjectInputStream in;

        while (true) {
            try {
                System.out.println("Accepting connections...");
                Socket socketTCP = serverTCP.accept();
                System.out.println("Connection accepted...");
                out = new ObjectOutputStream(socketTCP.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socketTCP.getInputStream());

                Message data = null;

                try {
                    data = (Message) in.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if(data.getType() == Message.AUTHENTICATE_USER) {
                    PictureWorkerRunnable pictureWorkerRunnable = new PictureWorkerRunnable(in);
                    pictureWorkerRunnable.setTemplate(template);
                    Thread newPictureClient = new Thread(pictureWorkerRunnable);
                    clientThreads.add(newPictureClient);
                    newPictureClient.start();

                    Thread newAudioClient = new Thread(new AudioWorkerRunnable(serverUDP));
                    clientThreads.add(newAudioClient);
                    newAudioClient.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
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
