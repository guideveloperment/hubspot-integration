package dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WebhookEvent {

	private String eventType;
	private long objectId;
	private long portalId;
	private Map<String, String> properties;
	public Object getEventType() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getObjectId() {
		// TODO Auto-generated method stub
		return null;
	}

}