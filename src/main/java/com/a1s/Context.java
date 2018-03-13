package com.a1s;

import io.reactivex.Flowable;
import io.reactivex.subjects.Subject;

public class Context {
    protected String callId;

    protected long abonent;

    protected Subject subject;

    protected Subject main;

    public Context(String callId, long abonent) {
        this.callId = callId;
        this.abonent = abonent;
    }

    public Subject getMain() {
        return main;
    }

    public void setMain(Subject main) {
        this.main = main;
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

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Context{");
        sb.append("callId='").append(callId).append('\'');
        sb.append(", abonent=").append(abonent);
        sb.append('}');
        return sb.toString();
    }
}
