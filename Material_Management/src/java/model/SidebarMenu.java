package model;

/**
 * Model đại diện cho một mục menu ở sidebar
 */
public class SidebarMenu {
    private int menuId; // ID của menu
    private String menuName; // Tên menu
    private String url; // Đường dẫn URL
    private String icon; // Tên icon
    private Integer parentId; // ID menu cha (nếu có)
    private Integer permissionId; // ID quyền

    // Getter và Setter cho từng thuộc tính
    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }

    public String getMenuName() { return menuName; }
    public void setMenuName(String menuName) { this.menuName = menuName; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }

    public Integer getPermissionId() { return permissionId; }
    public void setPermissionId(Integer permissionId) { this.permissionId = permissionId; }
} 