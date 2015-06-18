import java.io.*;
import java.util.*;

/**
* A class that represents a circular shift.
*/
public class CircularShift implements Comparable<CircularShift>{
	/**
	* The original line this is a shift of.
	*/
	Line originalLine;

	/**
	* The index of the word in the original line
	* that begins this shift.
	*/
	int startIndex;

	/**
	* The constructor to create a CircularShift
	* from an original Line and index of the word
	* in the Line that begins the shift.
	*/
	public CircularShift(Line line, int index) {
		this.originalLine = line;
		this.startIndex = index;
	}

	/**
	* Compares this shift to another.
	* Returns a negative int if this shift comes
	* alphabetically before other, 0 if both shifts
	* are equal, or a positive int if this shift comes
	* alphabetically after other.
	*/
	public int compareTo(CircularShift other) {
		return toString().compareTo(other.toString());
	}

	/**
	* Reconstructs and returns the entire circular shift
	* as a string.
	*/
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// Append words from start index to end of line
		for(int k=startIndex; k<originalLine.getNumWords(); k++) {
			builder.append(originalLine.getWord(k)).append(' ');
		}
		// Append words from start of line to just before start index
		for(int k=0; k<startIndex; k++) {
			builder.append(originalLine.getWord(k)).append(' ');
		}
		return builder.substring(0, builder.length() - 1);
	}
}