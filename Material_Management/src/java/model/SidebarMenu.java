package model;

/**
 * Model đại diện cho một mục menu ở sidebar
 * Chứa thông tin về menu như tên, đường dẫn, icon, quyền truy cập
 */
public class SidebarMenu {
    // ID duy nhất của menu
    private int menuId;
    
    // Tên hiển thị của menu (VD: "Danh Sách Vật Tư")
    private String menuName;
    
    // Đường dẫn URL khi click vào menu (VD: "/materialList")
    private String url;
    
    // Tên class icon FontAwesome (VD: "fas fa-box")
    private String icon;
    
    // ID của menu cha (null nếu là menu gốc)
    private Integer parentId;
    
    // ID của quyền cần thiết để xem menu này
    private Integer permissionId;

    // ========== GETTER VÀ SETTER ==========
    
    public int getMenuId() { 
        return menuId; 
    }
    
    public void setMenuId(int menuId) { 
        this.menuId = menuId; 
    }

    public String getMenuName() { 
        return menuName; 
    }
    
    public void setMenuName(String menuName) { 
        this.menuName = menuName; 
    }

    public String getUrl() { 
        return url; 
    }
    
    public void setUrl(String url) { 
        this.url = url; 
    }

    public String getIcon() { 
        return icon; 
    }
    
    public void setIcon(String icon) { 
        this.icon = icon; 
    }

    public Integer getParentId() { 
        return parentId; 
    }
    
    public void setParentId(Integer parentId) { 
        this.parentId = parentId; 
    }

    public Integer getPermissionId() { 
        return permissionId; 
    }
    
    public void setPermissionId(Integer permissionId) { 
        this.permissionId = permissionId; 
    }
} 