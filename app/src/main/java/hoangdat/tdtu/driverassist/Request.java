package hoangdat.tdtu.driverassist;

import java.io.Serializable;

public class Request implements Serializable {
    String requestId;
    String requestOwner;
    String requestAsk;
    String requestAnswer;
    Boolean requestFlag;
    String requestAnswerBy;

    public String getRequestAnswerBy() {
        return requestAnswerBy;
    }

    public void setRequestAnswerBy(String requestAnswerBy) {
        this.requestAnswerBy = requestAnswerBy;
    }

    public Request(String requestId, String requestOwner, String requestAsk, String requestAnswer, Boolean requestFlag, String requestAnswerBy) {
        this.requestId = requestId;
        this.requestOwner = requestOwner;
        this.requestAsk = requestAsk;
        this.requestAnswer = requestAnswer;
        this.requestFlag = requestFlag;
        this.requestAnswerBy = requestAnswerBy;
    }
    public Request() {}
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestOwner() {
        return requestOwner;
    }

    public void setRequestOwner(String requestOwner) {
        this.requestOwner = requestOwner;
    }

    public String getRequestAsk() {
        return requestAsk;
    }

    public void setRequestAsk(String requestAsk) {
        this.requestAsk = requestAsk;
    }

    public String getRequestAnswer() {
        return requestAnswer;
    }

    public void setRequestAnswer(String requestAnswer) {
        this.requestAnswer = requestAnswer;
    }

    public Boolean getRequestFlag() {
        return requestFlag;
    }

    public void setRequestFlag(Boolean requestFlag) {
        this.requestFlag = requestFlag;
    }
}
