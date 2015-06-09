import java.io.*;
import java.util.*;

public class ShiftOutputter {
	public void output(AlphabetizedShiftSet shifts) {
		for(CircularShift shift : shifts)
			System.out.println(shift);
	}
}