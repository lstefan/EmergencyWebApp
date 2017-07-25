package ro.pub.cs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.pub.cs.dto.IncidentRequestDTO;
import ro.pub.cs.model.Incident;
import ro.pub.cs.model.IncidentPriority;
import ro.pub.cs.model.IncidentType;
import ro.pub.cs.repository.IncidentRepository;

/**
 * Created by lstefan on 6/11/2016.
 */
@Service("incidentService")
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    private IncidentRepository eventRepository;

    @Override
    public Incident saveIncident(IncidentRequestDTO incident) {
        Incident incidentModel = new Incident();
        incidentModel.setType(IncidentType.getByName(incident.getType()));
        incidentModel.setPriority(IncidentPriority.getByName(incident.getPriority()));
        incidentModel.setInitialLatitude(incident.getInitialLatitude());
        incidentModel.setInitialLongitude(incident.getInitialLongitude());
        incidentModel.setDateCreated(incident.getDateCreated());

        return eventRepository.save(incidentModel);
    }

    @Override
    public Iterable<Incident> getAllIncidents() {
        return eventRepository.findAll();
    }

    @Override
    public Incident findInchident(Long incidentId) {
        return eventRepository.findOne(incidentId);
    }

    @Override
    public void deleteIncident(Long incidentId) {
        eventRepository.delete(incidentId);
    }
}
