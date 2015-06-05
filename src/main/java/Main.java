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

	public static class InputParser {
		public ArrayList<CircularShift> parse(InputStream input) {
			ArrayList<CircularShift> parsedInput = new ArrayList<>();
			Scanner scanner = new Scanner(input);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				parsedInput.add(new CircularShift(line.split(" ")));
			}
			return parsedInput;
		}
	}

	public static class CircularShift implements Comparable<CircularShift>{
		LinkedList<String> words;
		public CircularShift(String[] words) {
			this.words = new LinkedList<>(Arrays.asList(words));
		}

		public CircularShift(LinkedList<String> words) {
			this.words = words;
		}

		public CircularShift shift() {
			LinkedList<String> shiftedWords = (LinkedList<String>) words.clone();
			shiftedWords.addLast(shiftedWords.removeFirst());
			return new CircularShift(shiftedWords);
		}

		public Set<CircularShift> allShifts() {
			Set<CircularShift> shifts = new TreeSet<>();
			shifts.add(this);
			CircularShift lastShift = this;
			for(int k=1; k<words.size(); k++) {
				lastShift = lastShift.shift();
				shifts.add(lastShift);
			}
			return shifts;
		}

		public int compareTo(CircularShift other) {
			return toString().compareTo(other.toString());
		}

		public String toString() {
			StringBuilder builder = new StringBuilder();
			for(String word : words)
				builder.append(word).append(' ');
			return builder.substring(0, builder.length() - 1);
		}
	}

	public static class Alphabetizer extends TreeSet<CircularShift> {
		public ArrayList<CircularShift> alphabatize() {
			return new ArrayList<CircularShift>(this);
		}
	}

	public static class ShiftOutputter {
		public void output(ArrayList<CircularShift> shifts) {
			for(CircularShift shift : shifts)
				System.out.println(shift);
		}
	}
}