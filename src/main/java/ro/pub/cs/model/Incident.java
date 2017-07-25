package ro.pub.cs.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = "incident")
public class Incident {
	private Long id;
	private IncidentType type;
	private IncidentPriority priority;
	private Long dateCreated;
	private Long dateFinished;
	private String initialLatitude;
	private String initialLongitude;
	private User assignee;
	private User createdBy;
	private Content content;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Enumerated(EnumType.STRING)
	public IncidentType getType() {
		return type;
	}

	public void setType(IncidentType type) {
		this.type = type;
	}

	@Enumerated(EnumType.STRING)
	public IncidentPriority getPriority() {
		return priority;
	}

	public void setPriority(IncidentPriority priority) {
		this.priority = priority;
	}

	public Long getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Long dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Long getDateFinished() {
		return dateFinished;
	}

	public void setDateFinished(Long dateFinished) {
		this.dateFinished = dateFinished;
	}

	public String getInitialLatitude() {
		return initialLatitude;
	}

	public void setInitialLatitude(String initialLatitude) {
		this.initialLatitude = initialLatitude;
	}

	public String getInitialLongitude() {
		return initialLongitude;
	}

	public void setInitialLongitude(String initialLongitude) {
		this.initialLongitude = initialLongitude;
	}

	@ManyToOne
	@JoinColumn(name = "assignee_id")
	public User getAssignee() {
		return assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	@ManyToOne
	@JoinColumn(name = "creator_id")
	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "content_id")
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}
}
