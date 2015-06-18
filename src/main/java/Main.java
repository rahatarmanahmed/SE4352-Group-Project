import java.io.*;
import java.util.*;

/**
* The entry point of this program.
*/
public class Main {
	/**
	* The entry point of this program.
	*/
	public static void main(String[] args) {
		InputParser inputParser = new InputParser();
		AlphabetizedShiftSet alphaShifts = new AlphabetizedShiftSet();
		ShiftOutputter outputter = new ShiftOutputter();

		ArrayList<Line> lines = inputParser.parse(System.in);
		for(Line line : lines)
			for(CircularShift shift : line.getShifts())
				alphaShifts.add(shift);

		outputter.output(alphaShifts);
	}
}