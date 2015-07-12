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
        // Setup JDBC
        Class.forName("org.sqlite.JDBC");

        Gson gson = new Gson();

        // Server setup
        port(8000);
        externalStaticFileLocation("./public");

        /**
         * Endpoint to get all lines.
         */
		get("/index", (req, res) -> Database.getAll());

        /**
         * Endpoint to add new line. Accepts a JSON body with url and description properties.
         */
        post("/index", (req, res) -> {
            JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            if (Database.insert(new Line(body.get("url").getAsString(), body.get("description").getAsString()))) {
                return true;
            } else {
                res.status(500);
                return null;
            }
        });

        /**
         * Endpoint to delete a line. Accepts line id as a url parameter.
         */
        delete("/index/:id", (req, res) -> {
            if (Database.delete(Integer.parseInt(req.params(":id")))) {
                return true;
            } else {
                res.status(500);
                return null;
            }
        });

        /**
         * Endpoint to search for a line. Accepts a JSON body with keywords, operation, and caseSensitive properties.
         */
        post("/search", (req, res) -> {
            JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            String[] keywords = gson.fromJson(body.get("keywords"), String[].class);
            return Database.search(keywords, body.get("operation").getAsString(), body.get("caseSensitive").getAsBoolean());
        });

        /**
         * Endpoint to increment clicks for a line. Accepts a line id as a url parameter.
         */
        get("/click/:id", (req, res) -> {
            if (Database.incrementClicks(Integer.parseInt(req.params(":id")))) {
                return true;
            } else {
                res.status(500);
                return null;
            }
        });

        /**
         * Endpoint to get autocomplete suggestions. Accepts a JSON body with a query property.
         */
        post("/autocomplete", (req, res) -> {
            JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return Database.autoComplete(body.get("query").getAsString());
        });
	}
}
