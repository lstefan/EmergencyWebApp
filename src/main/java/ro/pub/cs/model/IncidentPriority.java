package ro.pub.cs.model;

/**
 *
 */
public enum IncidentPriority {
	HIGH ("High"),
	MEDIUM ("Medium"),
	LOW("Low");

	private final String priorityCode;

	private IncidentPriority(String priorityCode) {
		this.priorityCode = priorityCode;
	}

	public String getPriorityCode() {
		return priorityCode;
	}

	public static IncidentPriority getByName(String name){
		for(IncidentPriority incidentPriority : IncidentPriority.values()) {
			if(incidentPriority.getPriorityCode().equals(name)) {
				return incidentPriority;
			}
		}
		return null;
	}
}
