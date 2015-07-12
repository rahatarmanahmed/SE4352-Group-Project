import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the connection and communication with the SQLite database.
 */
public class Database {

    /**
     * Inserts a Line into the database.
     */
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

    /**
     * Deletes a line from the database.
     */
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

    /**
     * Returns all lines in the database.
     */
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

    /**
     * Searches for lines in the database that contain words in keywords.
     * Operation can be either "OR", "AND", or "NOT"
     * Case sensitive sets whether the search is case sensitive or not
     */
    public static synchronized JsonArray search(String[] keywords, String operation, Boolean caseSensitive) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:index.db")) {
            if(caseSensitive) connection.prepareStatement("PRAGMA case_sensitive_like = true").execute();
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

    /**
     * Increments the click counter on a line
     */
    public static synchronized boolean incrementClicks(int id) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:index.db")) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE lines SET clicks = clicks + 1 WHERE id = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Retrieves auto complete suggestions for a query.
     */
    public static synchronized JsonArray autoComplete(String query) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:index.db")) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM shifts WHERE shifted_description LIKE ? || '%'"
            );
            statement.setString(1, query);
            try (ResultSet rs = statement.executeQuery()) {
                JsonArray json = new JsonArray();
                while(rs.next()) {
                    JsonObject obj = new JsonObject();
                    obj.addProperty("lineId", rs.getString("lineId"));
                    obj.addProperty("id", rs.getString("id"));
                    obj.addProperty("shifted_description", rs.getString("shifted_description"));
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
