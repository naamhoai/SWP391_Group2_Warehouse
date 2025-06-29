package model;

public class Category {
    private Integer categoryId;
    private String name;
    private Integer parentId;
    private boolean hidden;

    public Category() {}

    public Category(Integer categoryId, String name, Integer parentId, boolean hidden) {
        this.categoryId = categoryId;
        this.name = name;
        this.parentId = parentId;
        this.hidden = hidden;
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

    @Override
    public String toString() {
        return "Category{" + "categoryId=" + categoryId + ", name=" + name + ", parentId=" + parentId + ", hidden=" + hidden + '}';
    }
}
