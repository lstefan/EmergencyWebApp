package ro.pub.cs.communication;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

public class TestSound {

    public static boolean saveToFile(AudioFileFormat.Type fileType, AudioInputStream audioInputStream) {
        String name = "src/main/resources/audio/sound";
        System.out.println("Saving...");
        if (null == name || null == fileType || audioInputStream == null) {
            System.out.println("E null");
            return false;
        }

        File myFile = new File(name + "." + fileType.getExtension());
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
}  