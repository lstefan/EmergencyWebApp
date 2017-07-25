package ro.pub.cs.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ro.pub.cs.dto.IncidentRequestDTO;
import ro.pub.cs.model.Incident;
import ro.pub.cs.service.IncidentService;

@RestController
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @RequestMapping(path = "/incidents", method = RequestMethod.POST)
    public ResponseEntity<?> createEvent(@RequestBody IncidentRequestDTO incident) {
        Incident createdIncident =  incidentService.saveIncident(incident);

        // Set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newPollUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdIncident.getId()).toUri();
        responseHeaders.setLocation(newPollUri);

        return new ResponseEntity<>(createdIncident.getId(), responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value= "/incidents", method= RequestMethod.GET)
    public ResponseEntity<Iterable<Incident>> getAllEvents() {
        Iterable<Incident> allIncidents = incidentService.getAllIncidents();
        return new ResponseEntity<>(allIncidents, HttpStatus.OK);
    }

    @RequestMapping(value="/incidents/{incidentId}", method= RequestMethod.GET)
    public ResponseEntity<?> getEvent(@PathVariable Long incidentId) {
        Incident incident = incidentService.findInchident(incidentId);
        return new ResponseEntity<>(incident, HttpStatus.OK);
    }

    @RequestMapping(value="/incidents/{incidentId}", method= RequestMethod.PUT)
    public ResponseEntity<?> updateEvent(@RequestBody IncidentRequestDTO incident, @PathVariable Long incidentId) {
        // Save the entity
        Incident e = incidentService.saveIncident(incident);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/incidents/{incidentId}", method= RequestMethod.DELETE)
    public ResponseEntity<?> deleteEvent(@PathVariable Long incidentId) {
        incidentService.deleteIncident(incidentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
