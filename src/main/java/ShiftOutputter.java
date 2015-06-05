import java.io.*;
import java.util.*;

public class ShiftOutputter {
	public void output(ArrayList<CircularShift> shifts) {
		for(CircularShift shift : shifts)
			System.out.println(shift);
	}
}