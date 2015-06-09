import java.io.*;
import java.util.*;

public class Line {
	private String[] words;

	public Line(String line) {
		words = line.split("\\s+");
	}

	public String getWord(int index) {
		return words[index];
	}

	public int getNumWords() {
		return words.length;
	}

	public Set<CircularShift> getShifts() {
		Set<CircularShift> shifts = new HashSet<>();
		for(int k=0; k<words.length; k++) {
			shifts.add(new CircularShift(this, k));
		}
		return shifts;
	}
}