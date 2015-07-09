import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {


    public static synchronized boolean insert(Line line) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:index.db")) {
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO lines (url, description) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, line.getUrl());
            statement.setString(2, line.getDescription());
            int rows = statement.executeUpdate();
            if(rows == 0) throw new SQLException("Inserting line failed");
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    for (CircularShift shift : line.getShifts()) {
                        statement = connection.prepareStatement(
                            "INSERT INTO shifts (lineId, shifted_description) VALUES (?, ?)"
                        );
                        statement.setInt(1, keys.getInt(1));
                        statement.setString(2, shift.toString());
                        rows = statement.executeUpdate();
                        if(rows == 0) throw new SQLException("Inserting shift failed");
                    }
                } else {
                    throw new SQLException("Inserting line failed, no ID obtained");
                }

            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static synchronized boolean delete(int id) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:index.db")) {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM lines WHERE id = ?",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            if(rows == 0) throw new SQLException("Deleting line failed");
            statement = connection.prepareStatement(
                    "DELETE FROM shifts WHERE lineId = ?",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, id);
            rows = statement.executeUpdate();
            if(rows == 0) throw new SQLException("Deleting shifts failed");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static synchronized JsonArray getAll() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:index.db")) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM lines"
            );
            try (ResultSet rs = statement.executeQuery()) {
                JsonArray json = new JsonArray();
                while(rs.next()) {
                    JsonObject obj = new JsonObject();
                    obj.addProperty("id", rs.getString("id"));
                    obj.addProperty("url", rs.getString("url"));
                    obj.addProperty("description", rs.getString("description"));
                    obj.addProperty("clicks", rs.getString("clicks"));
                    json.add(obj);
                }
                return json;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized JsonArray search(String[] keywords, String operation) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:index.db")) {
            String sql = "SELECT * FROM lines WHERE";
            for(int k=0; k<keywords.length; k++) {
                switch(operation.toUpperCase()) {
                    case "OR":
                        if(k != 0) sql += " OR";
                        sql += " id IN (SELECT lineId FROM shifts WHERE shifted_description LIKE ? || '%')";
                        break;
                    case "AND":
                        if(k != 0) sql += " AND";
                        sql += " id IN (SELECT lineId FROM shifts WHERE shifted_description LIKE ? || '%')";
                        break;
                    case "NOT":
                        if(k != 0) sql += " AND";
                        sql += " id NOT IN (SELECT lineId FROM shifts WHERE shifted_description LIKE ? || '%')";
                        break;
                }

            }
            PreparedStatement statement = connection.prepareStatement(sql);
            for(int k=1; k<=keywords.length; k++) {
                statement.setString(k, keywords[k-1]);
            }
            try (ResultSet rs = statement.executeQuery()) {
                JsonArray json = new JsonArray();
                while(rs.next()) {
                    JsonObject obj = new JsonObject();
                    obj.addProperty("id", rs.getString("id"));
                    obj.addProperty("url", rs.getString("url"));
                    obj.addProperty("description", rs.getString("description"));
                    obj.addProperty("clicks", rs.getString("clicks"));
                    json.add(obj);
                }
                return json;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
