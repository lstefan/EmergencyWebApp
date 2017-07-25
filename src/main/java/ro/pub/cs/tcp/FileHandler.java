package ro.pub.cs.tcp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandler {
    public void handleFile(byte[] frame) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("file.jpg");
            fos.write(frame);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
