package com.ricky.campus.model.dto;

public class SearchResult {
    private String type;
    private Long id;
    private String title;
    private String subInfo;

    public SearchResult(String type, Long id, String title, String subInfo) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.subInfo = subInfo;
    }

    public String getType() { return type; }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getSubInfo() { return subInfo; }
}
