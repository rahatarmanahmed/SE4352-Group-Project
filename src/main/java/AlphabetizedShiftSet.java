import java.io.*;
import java.util.*;

/**
* A set that keeps CircularShifts in alphabetic order.
* Does not allow duplicates.
* Extending TreeSet and ensuring CircularShifts implement
* compareTo is all we need to do to satisfy the architecture.
*/
public class AlphabetizedShiftSet extends TreeSet<CircularShift> {
}