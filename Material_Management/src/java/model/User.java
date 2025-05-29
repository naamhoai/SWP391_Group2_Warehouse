/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author kien3
 */
public class User {

    private int user_id;
    private String username;
    private String fullname;
    private String password;
    private String phone;
    private String email;
    private String status;
    private int role_id;
    private int priority;
    private String image;


    public User() {
    }

    public User(int user_id, String username, String fullname, String password, String phone, String email, String status, int role_id, int priority, String image) {
        this.user_id = user_id;
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.status = status;
        this.role_id = role_id;
        this.priority = priority;
        this.image = image;
  
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    

    @Override
    public String toString() {
        return "User{" + "user_id=" + user_id + ", username=" + username + ", fullname=" + fullname + ", password=" + password + ", phone=" + phone + ", email=" + email + ", status=" + status + ", role_id=" + role_id + ", priority=" + priority + ", image=" + image + '}';
    }
    

    

}
