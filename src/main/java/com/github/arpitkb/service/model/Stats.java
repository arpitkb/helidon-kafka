package com.github.arpitkb.service.model;


public class Stats {
    private long count;
    private String name;
    private long success;
    private long failure;

    private String rootId;
    private String id;
    private String parentId;
    private String description;

    public Stats(long count, String name, long success, long failure, String rootId,String id,String parentId,String description) {
        this.count = count;
        this.name = name;
        this.success = success;
        this.failure = failure;
        this.rootId=rootId;
        this.parentId=parentId;
        this.id=id;
        this.description=description;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSuccess() {
        return success;
    }

    public void setSuccess(long success) {
        this.success = success;
    }

    public long getFailure() {
        return failure;
    }

    public void setFailure(long failure) {
        this.failure = failure;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
