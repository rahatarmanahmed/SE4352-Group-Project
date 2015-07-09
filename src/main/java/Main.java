import com.google.gson.Gson;
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
        Gson gson = new Gson();
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
            if (Database.delete(Integer.parseInt(req.params(":id")))) {
                return true;
            } else {
                res.status(500);
                return null;
            }
        });
        post("/search", (req, res) -> {
            JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            String[] keywords = gson.fromJson(body.get("keywords"), String[].class);
            return Database.search(keywords, body.get("operation").getAsString(), body.get("caseSensitive").getAsBoolean());
        });
        get("/click/:id", (req, res) -> {
            if (Database.incrementClicks(Integer.parseInt(req.params(":id")))) {
                return true;
            } else {
                res.status(500);
                return null;
            }
        });

        post("/autocomplete", (req, res) -> {
            JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return Database.autoComplete(body.get("query").getAsString());
        });
	}
}