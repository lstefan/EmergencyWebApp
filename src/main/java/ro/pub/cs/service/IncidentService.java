package ro.pub.cs.service;


import ro.pub.cs.dto.IncidentRequestDTO;
import ro.pub.cs.model.Incident;

/**
 * Created by lstefan on 6/11/2016.
 */

public interface IncidentService {

    Incident saveIncident(IncidentRequestDTO incident);

    Iterable<Incident> getAllIncidents();

    Incident findInchident(Long incidentId);

    void deleteIncident(Long incidentId);
}
