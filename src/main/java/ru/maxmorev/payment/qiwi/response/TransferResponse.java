package ru.maxmorev.payment.qiwi.response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferResponse{
	
	private Transaction transaction;
	private String comment;
	
	public TransferResponse() {
		super();
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
