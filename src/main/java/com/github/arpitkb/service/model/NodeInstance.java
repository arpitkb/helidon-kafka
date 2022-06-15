package com.github.arpitkb.service.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeInstance {
    public String id;
    public String parentId;
    public String rootId;
    public List<String> stack = new ArrayList<>();
    public String name;
    public Object item;
    public NodeStatus status;
    public String start;
    public Long startEpochMs;
    public String end;
    public Long endEpochMs;
    public String restart;
    public Long restartEpochMs;

    public Integer attempt = 1;
    public Integer attempts = 1;
    public Boolean killed;
    public Integer timeout;

    public Map<String, Object> values = new HashMap<>();
    public Map<String, Object> mergedValues = new HashMap<>();
    public Map<String, Object> outputs = new HashMap<>();
    public Map<String, String> env = new HashMap<>();

    public String description;

    // currently only used as a placeholder to store failure message
// we'll want to be able to attribute execution details to the instance via properties
// resembling this one
    public List<String> messages = new ArrayList<>();

    // child instances of workflow
    public List<NodeInstance> nodes = new ArrayList<>();
    public List<NodeInstance> archive = new ArrayList<>();

    // different types of nodes
//    public NodeDefinition definition;
//    public ResourceLocator type;
//    public ApplicationDeclaration application;
//    public ResourceLocator chart;
//    public JavaNodeDeclaration java;
//    public ShellDeclaration shell;
//    public Container container;
//    public Pod pod;
//    public Volume[] volumes;
    public String image;

    // IO
//    public List<FileInjection> files = new ArrayList<>();

    public boolean proxy = false;
    public List<String> from = new ArrayList<>();

    public List<String> catchNodes = new ArrayList<>();

//    public ForeachInstance foreach;

//    public GitContextMap context = new GitContextMap();

    public Object metadata;

    public NodeInstance() {
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

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<NodeInstance> getNodes() {
        return nodes;
    }

    public void addNode(NodeInstance node) {
        node.setParentId(id);
        node.setRootId(id);
        nodes.add(node);
    }
}

