package io.github.almogtavor.mocks;

import io.github.almogtavor.annotations.DetailedPojo;

import java.util.Date;

@DetailedPojo
public class TargetFile {
    private String entityId;
    private Date createdDate;
    private String text;

    public TargetFile(String entityId, Date createdDate, String text) {
        this.entityId = entityId;
        this.createdDate = createdDate;
        this.text = text;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
