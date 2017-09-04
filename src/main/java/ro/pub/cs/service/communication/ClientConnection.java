package ro.pub.cs.service.communication;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {

    String requesterID;
    String doctorID;

    private Integer tcpPort = 9999;
    private Integer udpPort = 9998;
    private Socket tcpSocket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private Thread audioReaderThread;
    private Thread imageAndMessageReaderThread;
    private MessageWriter messageWriter;

    public ClientConnection() {}

    public ClientConnection(String requesterID, ObjectInputStream objectInputStream,
                            ObjectOutputStream objectOutputStream, Thread audioReaderThread,
                            Thread imageAndMessageReaderThread, MessageWriter messageWriter) {
        this.requesterID = requesterID;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.audioReaderThread = audioReaderThread;
        this.imageAndMessageReaderThread = imageAndMessageReaderThread;
        this.messageWriter = messageWriter;
    }

    public String getRequesterID() {
        return requesterID;
    }

    public void setRequesterID(String requesterID) {
        this.requesterID = requesterID;
    }

    public Integer getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(Integer tcpPort) {
        this.tcpPort = tcpPort;
    }

    public Integer getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(Integer udpPort) {
        this.udpPort = udpPort;
    }

    public Socket getTcpSocket() {
        return tcpSocket;
    }

    public void setTcpSocket(Socket tcpSocket) {
        this.tcpSocket = tcpSocket;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public void setObjectInputStream(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }

    public Thread getAudioReaderThread() {
        return audioReaderThread;
    }

    public void setAudioReaderThread(Thread audioReaderThread) {
        this.audioReaderThread = audioReaderThread;
    }

    public Thread getImageAndMessageReaderThread() {
        return imageAndMessageReaderThread;
    }

    public void setImageAndMessageReaderThread(Thread imageAndMessageReaderThread) {
        this.imageAndMessageReaderThread = imageAndMessageReaderThread;
    }

    public MessageWriter getMessageWriter() {
        return messageWriter;
    }

    public void setMessageWriter(MessageWriter messageWriter) {
        this.messageWriter = messageWriter;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }
}
