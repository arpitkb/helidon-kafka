package com.github.arpitkb.service.model;


public class NodeStatus {
    private String status;

    public NodeStatus(String status) {
        this.status = status;
    }

    public String get() {
        return status;
    }

    public void set(String status) {
        this.status = status;
    }
}
