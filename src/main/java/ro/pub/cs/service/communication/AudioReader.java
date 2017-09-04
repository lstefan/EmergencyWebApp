package ro.pub.cs.service.communication;


import message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class AudioReader implements Runnable {
    private final static Logger LOG = LoggerFactory.getLogger(AudioReader.class);

    private static final int BUFFER_SIZE = 1000;
    private static final int AUDIO_BUFFER_SIZE = 640;

    private static final int SAMPLE_RATE = 8000;
    private static final AudioFormat FORMAT = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);

    private DatagramSocket serverUDP;
    private AudioInputStream audioInputStream;

    private SimpMessagingTemplate template;

    public AudioReader(DatagramSocket serverUDP) {
        this.serverUDP = serverUDP;
    }

    @Override
    public void run() {
        LOG.info("Starting the audio reader");
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
            } catch (EOFException e) {
                //do nothing
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            //byte[] encoded = Base64.getEncoder().encode(receivedAudioData);
            //template.convertAndSend("/topic/audio", new BinaryMessage(receivedAudioData));


            out.write(receivedAudioData, 0, receivedAudioData.length);
            len++;
            //System.out.println("Total primit " + out.size());


            if (len == 20) {
//                try {
//                    out.flush();
//                    out.close();
//                } catch (final IOException ex) {
//                    ex.printStackTrace();
//                }

                //TestSound.ProcessAudio(out, FORMAT);
//                // load bytes into the audio input stream for playback
//                byte audioBytes[] = out.toByteArray();
//
//                //template.convertAndSend("/topic/audio", new BinaryMessage(audioBytes));
//
//                ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
//
//                int frameSizeInBytes = FORMAT.getFrameSize();
//                audioInputStream = new AudioInputStream(bais, FORMAT, audioBytes.length / frameSizeInBytes);
//
//                try {
//                    audioInputStream.reset();
//                } catch (final Exception ex) {
//                    ex.printStackTrace();
//                    return;
//                }
//
//                byte convertedAudioBytes[] = new byte[audioBytes.length / frameSizeInBytes];
//                try {
//                    audioInputStream.read(convertedAudioBytes);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                //template.convertAndSend("/topic/audio", new BinaryMessage(convertedAudioBytes));

//                try {
//                    TestSound.saveToFile(AudioFileFormat.Type.WAVE, audioInputStream);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }




                //re-initialize
                len = 0;
                out = new ByteArrayOutputStream();
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
