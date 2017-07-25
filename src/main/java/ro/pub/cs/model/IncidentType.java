package ro.pub.cs.model;

/**
 *
 */
public enum IncidentType {
	CAR_CRASH("Car crash"),
	FIRE("Fire"),
	EXPLOSION("Explosion"),
	DROWNING("Drowning"),
	SICKNESS("Sickness"),
	OTHER("Other");

	private final String typeCode;

	private IncidentType(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public static IncidentType getByName(String name){
		for(IncidentType incidentType : IncidentType.values()) {
			if(incidentType.getTypeCode().equals(name)) {
				return incidentType;
			}
		}
		return null;
	}
}
