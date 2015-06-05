import java.io.*;
import java.util.*;

public class CircularShift implements Comparable<CircularShift>{
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