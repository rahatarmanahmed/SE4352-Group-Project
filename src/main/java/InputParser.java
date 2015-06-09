import java.io.*;
import java.util.*;

public class InputParser {
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