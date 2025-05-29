package dao;

import model.TokenForgetPassword;
import dal.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DAOTokenForget extends DBContext {

    public boolean insertTokenForget(TokenForgetPassword tokenForget) {
        String sql = "INSERT INTO tokenforgetpassword (token, expiryTime, isUsed, user_id) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, tokenForget.getToken());
            ps.setTimestamp(2, Timestamp.valueOf(tokenForget.getExpiryTime()));
            ps.setBoolean(3, tokenForget.isUsed()); 
            ps.setInt(4, tokenForget.getUser_id()); 
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public TokenForgetPassword getTokenPassword(String token) {
        String sql = "SELECT * FROM tokenforgetpassword WHERE token = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, token);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new TokenForgetPassword(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getBoolean("isUsed"),
                            rs.getString("token"),
                            rs.getTimestamp("expiryTime").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateStatus(TokenForgetPassword token) {
        String sql = "UPDATE tokenforgetpassword SET isUsed = ? WHERE token = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setBoolean(1, token.isUsed()); // sửa
            st.setString(2, token.getToken());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
