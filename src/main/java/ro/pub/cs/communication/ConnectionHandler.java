package ro.pub.cs.communication;

import message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ro.pub.cs.service.IncidentService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ConnectionHandler implements Runnable {

    private final static Logger LOG = LoggerFactory.getLogger(ConnectionHandler.class);

    public static final int TCP_PORT = 9999;
    public static final int UDP_PORT = 9998;

    private Map<String, ClientConnection> clientConnectionMap;

    private SimpMessagingTemplate template;

    private IncidentService incidentService;

    @Override
    public void run() {
        LOG.info("Starting the connection listener");

        clientConnectionMap = new HashMap<>();

        ServerSocket serverTCP = null;
        DatagramSocket serverUDP = null;

        LOG.info("Creating sockets");
        try {
            serverTCP = new ServerSocket(TCP_PORT);
            serverUDP = new DatagramSocket(UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (true) {
            try {
                LOG.info("Accepting connections...");
                Socket socketTCP = serverTCP.accept();
                LOG.info("Connection accepted with {} : ", socketTCP.getInetAddress());
                ObjectOutputStream out =  new ObjectOutputStream(socketTCP.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(socketTCP.getInputStream());

                Message data = null;
                try {
                    data = (Message) in.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if(data.getType() == Message.AUTHENTICATE_USER) {
                    LOG.info("Received an AUTHENTICATE_USER message from {} : ", socketTCP.getInetAddress());
                    ImageAndMessageReader imageAndMessageReader = new ImageAndMessageReader(in);
                    imageAndMessageReader.setTemplate(template);
                    imageAndMessageReader.setIncidentService(incidentService);
                    Thread imageAndMessageReaderThread = new Thread(imageAndMessageReader);
                    imageAndMessageReaderThread.start();

                    AudioReader audioReader = new AudioReader(serverUDP);
                    audioReader.setTemplate(template);
                    Thread audioReaderThread = new Thread(audioReader);
                    audioReaderThread.start();

                    MessageWriter messageWriter = new MessageWriter(out);
                    messageWriter.setTemplate(template);

                    ClientConnection clientConnection = new ClientConnection(data.getRequesterID(), in, out,
                            audioReaderThread, imageAndMessageReaderThread, messageWriter);
                    clientConnectionMap.put(data.getRequesterID(), clientConnection);
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

    public void setIncidentService(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    public Map<String, ClientConnection> getClientConnectionMap() {
        return clientConnectionMap;
    }

    public void setClientConnectionMap(Map<String, ClientConnection> clientConnectionMap) {
        this.clientConnectionMap = clientConnectionMap;
    }
}
