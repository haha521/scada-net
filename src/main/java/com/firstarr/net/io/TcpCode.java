package com.firstarr.net.io;

public class TcpCode {

    private String code;

    private volatile boolean isFirst = true;

    private volatile boolean isOpen = true;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }
}
