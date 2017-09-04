package ro.pub.cs.service.communication;

import message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.BinaryMessage;
import ro.pub.cs.dto.IncidentRequestDTO;
import ro.pub.cs.model.Incident;
import ro.pub.cs.service.IncidentService;

import java.io.*;

public class ImageAndMessageReader implements Runnable {
    private final static Logger LOG = LoggerFactory.getLogger(ImageAndMessageReader.class);

    private ObjectInputStream input;
    private Message data;

    public ImageAndMessageReader(ObjectInputStream input) {
        this.input = input;
    }

    private SimpMessagingTemplate template;

    private IncidentService incidentService;

    @Override
    public void run() {
        LOG.info("Starting the image and message reader");

        while (true) {
            try {
                data = (Message) input.readObject();
                readData();
            } catch (EOFException e) {
                //do nothing
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readData() {
        final long time = System.currentTimeMillis();
        if (data.getType() == Message.SEND_IMAGE) {
            LOG.info("Received message of type SEND_IMAGE");
            byte[] pictureData = data.getPicture();

            template.convertAndSend("/topic/frame", new BinaryMessage(pictureData));

            writePictureToDisk(time, pictureData);
        } else if(data.getType() == Message.REQUEST_DOCTOR) {
            LOG.info("Received message of type REQUEST_DOCTOR");
            IncidentRequestDTO incidentRequestDTO = new IncidentRequestDTO();
            incidentRequestDTO.setType(data.getIncidentType());
            incidentRequestDTO.setDateCreated(data.getDateCreated());
            incidentRequestDTO.setInitialLatitude(data.getInitialLatitude());
            incidentRequestDTO.setInitialLongitude(data.getInitialLongitude());
            incidentRequestDTO.setPriority(data.getPriority());

            Incident createdIncident =  incidentService.saveIncident(incidentRequestDTO);

            //TODO: retrieve logged-in users that are not busy, choose one and send request, otherwise send reject-call
            //template.convertAndSend("/topic/incident", createdIncident);
            template.convertAndSend("/topic/incident", data);
        } else if(data.getType() == Message.SEND_MESSAGE) {
            LOG.info("Received message of type SEND_MESSAGE");
            template.convertAndSend("/topic/chat", data);
        }
        LOG.info("Request processed: " + time);
    }

    private void writePictureToDisk(long time, byte[] pictureData) {
        if (pictureData.length != 0) {
            OutputStream fileOutputStream = null;
            String fileName = "src/main/resources/img/img" + time + ".jpg";
            try {
                fileOutputStream = new BufferedOutputStream(new FileOutputStream(fileName));
                fileOutputStream.write(pictureData);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
}