package Objects;

import org.json.JSONObject;

public class MailMessage {
	
	private String from;
	private String recipients;
	private JSONObject data;
	private String type;
	private JSONObject jsonObject;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    private String subject;
	
	public MailMessage(String from, String Recipients,String data, String type,String subject) throws Exception
	{
		this.from=from;
		this.recipients=Recipients;
		this.data=new JSONObject(data);
		this.type=type;
        this.subject = subject;
	}
	
	
	
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getRecipients() {
		return recipients;
	}
	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}
	public JSONObject getData() {
		return data;
	}
	public void setData(JSONObject data) {
		this.data = data;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
