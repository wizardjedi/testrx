package com.a1s;

import io.reactivex.Flowable;

public class Context {
    protected String callId;

    protected long abonent;

    protected Flowable flowable;

    public Context(String callId, long abonent) {
        this.callId = callId;
        this.abonent = abonent;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public long getAbonent() {
        return abonent;
    }

    public void setAbonent(long abonent) {
        this.abonent = abonent;
    }

    public Flowable getFlowable() {
        return flowable;
    }

    public void setFlowable(Flowable flowable) {
        this.flowable = flowable;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Context{");
        sb.append("callId='").append(callId).append('\'');
        sb.append(", abonent=").append(abonent);
        sb.append(", flowable=").append(flowable);
        sb.append('}');
        return sb.toString();
    }
}
