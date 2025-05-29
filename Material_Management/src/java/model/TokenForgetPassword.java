package model;

import java.time.LocalDateTime;

public class TokenForgetPassword {
    private int id;
    private int user_id;
    private boolean isUsed;
    private String token;
    private LocalDateTime expiryTime;

    public TokenForgetPassword() {
    }

    public TokenForgetPassword(int id, int user_id, boolean isUsed, String token, LocalDateTime expiryTime) {
        this.id = id;
        this.user_id = user_id;
        this.isUsed = isUsed;
        this.token = token;
        this.expiryTime = expiryTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    @Override
    public String toString() {
        return "TokenForgetPassword{" + "id=" + id + ", user_id=" + user_id + ", isUsed=" + isUsed + ", token=" + token + ", expiryTime=" + expiryTime + '}';
    }
}
