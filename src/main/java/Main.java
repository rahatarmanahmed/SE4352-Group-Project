import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.*;
import java.sql.*;
import static spark.Spark.*;

/**
* The entry point of this program.
*/
public class Main {
	/**
	* The entry point of this program.
	*/
	public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        port(8000);
        externalStaticFileLocation("./public");
        // TODO: make endpoints
		get("/index", (req, res) -> Database.getAll());
        post("/index", (req, res) -> {
            JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            if (Database.insert(new Line(body.get("url").getAsString(), body.get("description").getAsString()))) {
                return true;
            } else {
                res.status(500);
                return null;
            }
        });
        delete("/index/:id", (req, res) -> {
            if(Database.delete(Integer.parseInt(req.params(":id")))) {
                return true;
            } else {
                res.status(500);
                return null;
            }
        });

	}
}