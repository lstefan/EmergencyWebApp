package ro.pub.cs.util;

import javax.sound.sampled.*;
import java.io.*;

public class TestSound {

    public static boolean saveToFile(AudioFileFormat.Type fileType, AudioInputStream audioInputStream) throws FileNotFoundException {
        String name = "src/main/resources/audio/sound" + "." + fileType.getExtension();
        System.out.println("Saving...");
        if (null == name || null == fileType || audioInputStream == null) {
            System.out.println("E null");
            return false;
        }

        File myFile = new File(name);
        // reset to the beginnning of the captured data
        try {
            audioInputStream.reset();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            AudioSystem.write(audioInputStream, fileType, myFile);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        System.out.println("Saved " + myFile.getAbsolutePath());
        return true;
    }

    public static void ProcessAudio(ByteArrayOutputStream out, AudioFormat format) {
        // load bytes into the audio input stream for playback
        byte audioBytes[] = out.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);


        //int frameSizeInBytes = format.getFrameSize();
        AudioInputStream audioInputStream = new AudioInputStream(bais, format, audioBytes.length);
        //long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / format.getFrameRate());
        //double duration = milliseconds / 1000.0;
        //System.out.println(duration);
        try {
            audioInputStream.reset();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return;
        }

        toSpeaker(audioBytes, format);
    }

    public static void toSpeaker(final byte soundbytes[], AudioFormat format){
        System.out.println("Thread toSpeaker " + Thread.activeCount());
        Thread thread = new Thread("ToSpeakers") {
            public void run(){
                try {
                    DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
                    SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

                    sourceDataLine.open(format);

                    FloatControl volumeControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
                    volumeControl.setValue(5);

                    sourceDataLine.start();
                    sourceDataLine.open(format);

                    sourceDataLine.start();

                    //System.out.println("format? :" + sourceDataLine.getFormat());

                    sourceDataLine.write(soundbytes, 0, soundbytes.length);
                    //System.out.println(soundbytes.toString());
                    sourceDataLine.drain();
                    sourceDataLine.close();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    System.out.println("Not working in speakers...");
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }
}  