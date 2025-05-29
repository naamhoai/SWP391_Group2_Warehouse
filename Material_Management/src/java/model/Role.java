/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author kien3
 */
public class Role {
    private  int roleid;
    private String rolename;

    public Role() {
    }

    public Role(int roleid, String rolename) {
        this.roleid = roleid;
        this.rolename = rolename;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    @Override
    public String toString() {
        return "Role{" + "roleid=" + roleid + ", rolename=" + rolename + '}';
    }
    

}
