package ro.pub.cs.dto;

import java.util.Date;

public class OutputMessageDTO extends MessageDTO {

	private Date time;
	
	public OutputMessageDTO(MessageDTO original, Date time) {
		super(original.getId(), original.getMessage());
		this.time = time;
	}
	
	public Date getTime() {
		return time;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
}
