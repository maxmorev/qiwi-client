package ru.maxmorev.payment.qiwi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonIgnoreProperties(ignoreUnknown = true)
public class State {

private String code;

public String getCode() {
return code;
}

public void setCode(String code) {
this.code = code;
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
