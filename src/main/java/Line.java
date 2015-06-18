import java.io.*;
import java.util.*;

/**
* A class that represents an input line.
*/
public class Line {
	/**
	* The internal storage for the words of the line.
	*/
	private String[] words;

	/**
	* The constructor to create a line from an input line.
	*/
	public Line(String line) {
		words = line.split("\\s+");
	}

	/**
	* Returns the word in this Line at the given index.
	*/
	public String getWord(int index) {
		return words[index];
	}

	/**
	* Returns the number of words in this Line.
	*/
	public int getNumWords() {
		return words.length;
	}

	/**
	* Returns a Set of all possible CircularShifts
	* of this Line.
	*/
	public Set<CircularShift> getShifts() {
		Set<CircularShift> shifts = new HashSet<>();
		for(int k=0; k<words.length; k++) {
			shifts.add(new CircularShift(this, k));
		}
		return shifts;
	}
}