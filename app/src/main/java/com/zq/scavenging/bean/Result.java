package com.zq.scavenging.bean;

/**
 * Created by sxj on 2016/9/29.
 */
public class Result {
    private String message;
    private int error_code;
    private Object content;

    public String getMessage() {
        if (message == null) {
            return "";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
