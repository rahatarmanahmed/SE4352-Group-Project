import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		InputParser inputParser = new InputParser();
		ShiftOutputter outputter = new ShiftOutputter();
		ArrayList<CircularShift> input = inputParser.parse(System.in);
		Alphabetizer alphabetizer = new Alphabetizer();
		for(CircularShift i : input)
			alphabetizer.addAll(i.allShifts());
		outputter.output(alphabetizer.alphabatize());
	}
}