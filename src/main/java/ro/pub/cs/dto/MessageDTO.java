package ro.pub.cs.dto;

public class MessageDTO {

  private String message;
  private int id;
  
  public MessageDTO() {
    
  }
  
  public MessageDTO(int id, String message) {
    this.id = id;
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
