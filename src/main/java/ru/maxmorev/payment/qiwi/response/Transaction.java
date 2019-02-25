package ru.maxmorev.payment.qiwi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

private String id;
private State state;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public State getState() {
return state;
}

public void setState(State state) {
this.state = state;
}

@Override
public String toString() {
	ObjectMapper mapper = new ObjectMapper();
	
	String jsonStr = "";
	try {
		jsonStr = mapper.writeValueAsString(this);
	}catch(Exception e) {
		
	}
	return jsonStr;

}


}
