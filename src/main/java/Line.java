import java.io.*;
import java.util.*;

/**
* A class that represents an input line.
*/
public class Line {

	/**
	 * The url of the index
	 */
	private String url;

	/**
	* The internal storage for the words of the line.
	*/
	private String[] words;

	/**
	 * The number of times the url was clicked.
	 */
	private int clicks = 0;

	/**
	* The constructor to create a line from an input line.
	*/
	public Line(String url, String description) {
		this.url = url;
		this.words = description.split("\\s+");
	}

    /**
     * Returns the url of this input line.
     */
	public String getUrl() {
		return url;
	}

    /**
     * Returns the description of this input line
     */
	public String getDescription() {
		return String.join(" ", words);
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
			CircularShift shift = new CircularShift(this, k);
			if(!NoiseFilter.isNoise(shift))
				shifts.add(shift);
		}
		return shifts;
	}
}
