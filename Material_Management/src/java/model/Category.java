package model;

import java.sql.Timestamp;

public class Category {
    private Integer categoryId;
    private String name;
    private Integer parentId;
    private boolean hidden;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Category() {}

    public Category(Integer categoryId, String name, Integer parentId, boolean hidden, Timestamp createdAt, Timestamp updatedAt) {
        this.categoryId = categoryId;
        this.name = name;
        this.parentId = parentId;
        this.hidden = hidden;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Category{" + "categoryId=" + categoryId + ", name=" + name + ", parentId=" + parentId + ", hidden=" + hidden + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}
