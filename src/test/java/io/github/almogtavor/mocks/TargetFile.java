package io.github.almogtavor.mocks;

import io.github.almogtavor.annotations.DetailedPojo;

import java.util.Date;

@DetailedPojo
public class TargetFile {
    private String itemId;
    private String parentId;
    private Date receptionTime;
    private String text;
    private String html;

    public TargetFile(String itemId, String parentId, Date receptionTime, String text, String html) {
        this.itemId = itemId;
        this.parentId = parentId;
        this.receptionTime = receptionTime;
        this.text = text;
        this.html = html;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Date getReceptionTime() {
        return receptionTime;
    }

    public void setReceptionTime(Date receptionTime) {
        this.receptionTime = receptionTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
