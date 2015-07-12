/**
 * Filters CircularShifts based on a blacklist of words.
 */
public class NoiseFilter {
    /**
     * The black list of words to filter.
     */
    public static final String[] NOISE_WORDS = {"a", "the", "of"};

    /**
     * Returns true if shift starts with a noise word.
     */
    public static boolean isNoise(CircularShift shift) {
        String shiftString = shift.toString();
        for(String word : NOISE_WORDS)
            if(shift.toString().toLowerCase().startsWith(word.toLowerCase()))
                return true;
        return false;
    }
}
