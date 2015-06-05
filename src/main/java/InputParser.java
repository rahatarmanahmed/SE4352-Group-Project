import java.io.*;
import java.util.*;

public class InputParser {
	public ArrayList<CircularShift> parse(InputStream input) {
		ArrayList<CircularShift> parsedInput = new ArrayList<>();
		Scanner scanner = new Scanner(input);
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			parsedInput.add(new CircularShift(line.split("\\s+")));
		}
		return parsedInput;
	}
}