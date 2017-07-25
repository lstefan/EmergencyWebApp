package ro.pub.cs.model;

/**
 *
 */
public enum DomainType {
	AUDIOLOGIST("Audiologist"),
	ALLERGIST("Allergist"),
	CARDIOLOGIST("Cardiologist"),
	DERMATOLOGIST("Dermatologist"),
	EMERGENCY_DOCTOR("Emergency Doctor"),
	ENDOCRINOLOGIST("Endocrinologist"),
	EPIDEMIOLOGIST("Epidemiologist"),
	GENERAL("General Practitioner"),
	HEPATOLOGIST("Hepatologist"),
	INFECTIOUS_DISEASE_SPECIALIST("Infectious Disease Specialist"),
	NEUROPHYSIOLOGIST("Neurophysiologist"),
	OBSTETRICIAN("Obstetrician"),
	ONCOLOGIST("Oncologist"),
	ORTHOPEDIST("Orthopedist"),
	PSYCHIATRIST("Psychiatrist");

	private final String typeCode;

	private DomainType(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public static DomainType getByName(String name){
		return DomainType.valueOf(name);
	}
}
