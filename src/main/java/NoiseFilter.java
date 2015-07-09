public class NoiseFilter {
    public static final String[] NOISE_WORDS = {"a", "the", "of"};
    public static boolean isNoise(CircularShift shift) {
        String shiftString = shift.toString();
        for(String word : NOISE_WORDS)
            if(shift.toString().toLowerCase().startsWith(word.toLowerCase()))
                return true;
        return false;
    }
}
