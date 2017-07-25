package ro.pub.cs.repository;

import org.springframework.data.repository.CrudRepository;

import ro.pub.cs.model.Incident;

/**
 * Created by lstefan on 6/11/2016.
 */
public interface IncidentRepository extends CrudRepository<Incident, Long> {
}
