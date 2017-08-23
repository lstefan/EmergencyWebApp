package ro.pub.cs.communication;


import message.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 */
public class AudioWorkerRunnable implements Runnable {
    private static final int BUFFER_SIZE = 1000;
    private static final int AUDIO_BUFFER_SIZE = 640;

    private static final int SAMPLE_RATE = 8000;
    private static final AudioFormat FORMAT = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);

    private DatagramSocket serverUDP;
    private AudioInputStream audioInputStream;

    public AudioWorkerRunnable(DatagramSocket serverUDP) {
        this.serverUDP = serverUDP;
    }

    @Override
    public void run() {
        System.out.println("Starting the audio reader");
        byte[] receivedMessageData = new byte[BUFFER_SIZE];
        byte[] receivedAudioData = new byte[AUDIO_BUFFER_SIZE];

        DatagramPacket pack = new DatagramPacket(receivedMessageData, receivedMessageData.length);
        int len = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        while (true) {
            try {
                serverUDP.receive(pack);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(receivedMessageData));
                Message messageClass = (Message) iStream.readObject();
                receivedAudioData = messageClass.getAudio();
                iStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            len++;
            out.write(receivedAudioData, 0, receivedAudioData.length);
            //System.out.println("Total primit " + out.size());

            if (len == 200) {
                try {
                    out.flush();
                    out.close();
                } catch (final IOException ex) {
                    ex.printStackTrace();
                }

                // load bytes into the audio input stream for playback
                byte audioBytes[] = out.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);

                int frameSizeInBytes = FORMAT.getFrameSize();
                audioInputStream = new AudioInputStream(bais, FORMAT, audioBytes.length / frameSizeInBytes);

                try {
                    audioInputStream.reset();
                } catch (final Exception ex) {
                    ex.printStackTrace();
                    return;
                }

                TestSound.saveToFile(AudioFileFormat.Type.WAVE, audioInputStream);

                //re-initialize
                len = 0;
                out = new ByteArrayOutputStream();
            }
        }

    }
}
