package pl.coderslab;

import pl.coderslab.utils.BCrypt;
import pl.coderslab.utils.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String READ_USER_QUERY = "SELECT * FROM users WHERE id =?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username =?, email =?, password=? WHERE id =?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id =?";
    private static final String FINDALL_USER_QUERY = "SELECT * FROM users";

    public User create(User user) {

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, hashPassword(user.getPassword()));
            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            System.out.println("Created user with id = " + user.getId());
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public User read(int userId) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(READ_USER_QUERY)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            User user = new User();
            if (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            }
            if (user.getId() == 0) {
                return null;
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(User user) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_USER_QUERY)) {
            if (user == null) {
                System.out.println("User does not exist");
            } else {
                if (read(user.getId()) != null) {
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, hashPassword(user.getPassword()));
                    ps.setInt(4, user.getId());
                    ps.executeUpdate();
                    System.out.println("Updated user with id = " + user.getId());
                } else {
                    System.out.println("User with id=" + user.getId() + " does not exist");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int userId) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_USER_QUERY)) {
            if (read(userId) != null) {
                ps.setInt(1, userId);
                ps.executeUpdate();
                System.out.println("Deleted user with id = " + userId);
            } else {
                System.out.println("User with id=" + userId + " does not exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> findAll() {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FINDALL_USER_QUERY)) {
            ResultSet rs = ps.executeQuery();
            List<User> list = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                list.add(user);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}
