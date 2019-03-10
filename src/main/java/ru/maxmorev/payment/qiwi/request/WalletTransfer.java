package ru.maxmorev.payment.qiwi.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.maxmorev.payment.qiwi.response.Amount;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletTransfer {

    private String id;
    private Amount sum;
    private String source = "account_643";
    private PaymentMethod paymentMethod;
    private String comment;
    private Field fields;

    public WalletTransfer(){
        super();
        id = String.valueOf( new Date().getTime()*1000 );
        paymentMethod = new PaymentMethod();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Amount getSum() {
        return sum;
    }

    public void setSum(Amount sum) {
        this.sum = sum;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Field getFields() {
        return fields;
    }

    public void setFields(Field fields) {
        this.fields = fields;
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
