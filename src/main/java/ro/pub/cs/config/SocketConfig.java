package ro.pub.cs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ro.pub.cs.communication.ConnectionHandler;
import ro.pub.cs.service.IncidentService;

@Configuration
public class SocketConfig {

    @Autowired
    public SimpMessagingTemplate template;

    @Autowired
    public IncidentService incidentService;

    @Bean
    public ConnectionHandler connectionListener() {
        ConnectionHandler connectionHandler = new ConnectionHandler();
        connectionHandler.setTemplate(template);
        connectionHandler.setIncidentService(incidentService);
        Thread connectionThread = new Thread(connectionHandler);
        connectionThread.start();

        return connectionHandler;
    }


//    private Thread createPictureThread(ObjectInputStream objectInputStream) {
//        ImageAndMessageReader pictureWorkerRunnable = new ImageAndMessageReader(objectInputStream);
//        Thread pictureWorkerThread = new Thread(pictureWorkerRunnable);
//
//        return pictureWorkerThread;
//    }
//
//    private Thread createAudioThread(DatagramSocket datagramSocket) {
//        AudioReader audioWorkerRunnable = new AudioReader(datagramSocket);
//        Thread audioWorkerThread = new Thread(audioWorkerRunnable);
//
//        return audioWorkerThread;
//    }

}
