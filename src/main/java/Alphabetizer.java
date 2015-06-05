import java.io.*;
import java.util.*;

public class Alphabetizer extends TreeSet<CircularShift> {
	public ArrayList<CircularShift> alphabatize() {
		return new ArrayList<CircularShift>(this);
	}
}