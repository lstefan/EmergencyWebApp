package ro.pub.cs.model;

/**
 *
 */
public enum RoleType {
	ADMIN("admin"),
	DOCTOR("doctor"),
	PARAMEDIC("paramedic");

	private final String typeCode;

	private RoleType(String type) {
		this.typeCode = type;
	}

	public String getTypeCode() { return typeCode; }
}
