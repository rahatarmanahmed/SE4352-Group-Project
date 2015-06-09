import java.io.*;
import java.util.*;

public class CircularShift implements Comparable<CircularShift>{
	Line originalLine;
	int startIndex;
	public CircularShift(Line line, int index) {
		this.originalLine = line;
		this.startIndex = index;
	}

	public int compareTo(CircularShift other) {
		return toString().compareTo(other.toString());
	}

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