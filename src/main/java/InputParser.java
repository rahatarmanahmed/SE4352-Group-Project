import java.io.*;
import java.util.*;

/**
* A module that parses the input lines into
* Line objects.
*/
public class InputParser {
	/**
	* Parses the input stream and returns a List of
	* Lines.
	*/
	public ArrayList<Line> parse(InputStream input) {
		ArrayList<Line> parsedInput = new ArrayList<>();
		Scanner scanner = new Scanner(input);
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			parsedInput.add(new Line(line));
		}
		return parsedInput;
	}
}