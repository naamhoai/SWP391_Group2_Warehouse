package model;

public class User {

    private int user_id;
    private String fullname;
    private String email;
    private String password;
    private String phone;
    private Role role;
    private String status;
    private int priority;
    private String image;
    private String gender;
    private String dayofbirth;
    private String description;

    public User() {
    }

    public User(int user_id, String fullname, String email, String password, String phone, Role role, String status, int priority, String image, String gender, String dayofbirth, String description) {
        this.user_id = user_id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.priority = priority;
        this.image = image;
        this.gender = gender;
        this.dayofbirth = dayofbirth;
        this.description = description;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDayofbirth() {
        return dayofbirth;
    }

    public void setDayofbirth(String dayofbirth) {
        this.dayofbirth = dayofbirth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "User{" + "user_id=" + user_id + ", fullname=" + fullname + ", email=" + email + ", password=" + password + ", phone=" + phone + ", role=" + role + ", status=" + status + ", priority=" + priority + ", image=" + image + ", gender=" + gender + ", dayofbirth=" + dayofbirth + ", description=" + description + '}';
    }

}
