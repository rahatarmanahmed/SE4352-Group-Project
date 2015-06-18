import java.io.*;
import java.util.*;

/**
* A module that outputs CircularShifts
*/
public class ShiftOutputter {
	/**
	* Prints out every shift in the given AlphabetizedShiftSet
	* in order
	*/
	public void output(AlphabetizedShiftSet shifts) {
		for(CircularShift shift : shifts)
			System.out.println(shift);
	}
}