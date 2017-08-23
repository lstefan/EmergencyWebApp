package ro.pub.cs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ro.pub.cs.communication.ConnectionListener;

import javax.annotation.Resource;

@Configuration
public class SocketConfig {

    @Autowired
    public SimpMessagingTemplate template;

    @Bean
    public ConnectionListener connectionListener() {
        ConnectionListener connectionListener = new ConnectionListener();
        connectionListener.setTemplate(template);
        Thread connectionThread = new Thread(connectionListener);
        connectionThread.start();

        return connectionListener;
    }


//    private Thread createPictureThread(ObjectInputStream objectInputStream) {
//        PictureWorkerRunnable pictureWorkerRunnable = new PictureWorkerRunnable(objectInputStream);
//        Thread pictureWorkerThread = new Thread(pictureWorkerRunnable);
//
//        return pictureWorkerThread;
//    }
//
//    private Thread createAudioThread(DatagramSocket datagramSocket) {
//        AudioWorkerRunnable audioWorkerRunnable = new AudioWorkerRunnable(datagramSocket);
//        Thread audioWorkerThread = new Thread(audioWorkerRunnable);
//
//        return audioWorkerThread;
//    }

}
