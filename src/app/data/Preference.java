package app.data;

import java.util.Arrays;

/**
 * Created by Vladimir on 17/02/18.
 **/
public class Preference {
    private static final int MONDAY = 0, TUESDAY = 1, WEDNESDAY = 2, THURSDAY = 3, FRIDAY = 4, SATURDAY = 5, COUNT = 6;
    private boolean[] days;

    public Preference(boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday) {
        days = new boolean[COUNT];
        days[MONDAY] = monday;
        days[TUESDAY] = tuesday;
        days[WEDNESDAY] = wednesday;
        days[THURSDAY] = thursday;
        days[FRIDAY] = friday;
        days[SATURDAY] = saturday;
    }

    public boolean getByIndex(int index) {
        if (index >= 0 && index <= SATURDAY) {
            return days[index];
        } else {
            throw new IllegalArgumentException("Must be 0..5 (0 - Monday, 1 - Tuesday,..., 5 - Saturday), but get {" + index + "}");
        }
    }

    public boolean isMonday() {
        return days[MONDAY];
    }

    public boolean isTuesday() {
        return days[TUESDAY];
    }

    public boolean isWednesday() {
        return days[WEDNESDAY];
    }

    public boolean isThursday() {
        return days[THURSDAY];
    }

    public boolean isFriday() {
        return days[FRIDAY];
    }

    public boolean isSaturday() {
        return days[SATURDAY];
    }

    public String getData() {
        return String.valueOf(deParseChar(isMonday())) + deParseChar(isTuesday()) + deParseChar(isWednesday()) +
                deParseChar(isThursday()) + deParseChar(isFriday()) + deParseChar(isSaturday());
    }

    public static Preference parsePreference(String line) {
        if (line.length() != 6) throw new IllegalArgumentException("Argument must be 6 char, but get: {" + line + "}");
        return new Preference(
                parseChar(line.charAt(0)), parseChar(line.charAt(1)),
                parseChar(line.charAt(2)), parseChar(line.charAt(3)),
                parseChar(line.charAt(4)), parseChar(line.charAt(5))
        );

    }

    private static boolean parseChar(char c) {
        return c != '0';
    }

    private static char deParseChar(boolean b) {
        return b ? '1' : '0';
    }

    @Override
    public String toString() {
        return "Preference{" +
                "M=" + isMonday() +
                ", T=" + isTuesday() +
                ", W=" + isWednesday() +
                ", T=" + isThursday() +
                ", F=" + isFriday() +
                ", S=" + isSaturday() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Preference that = (Preference) o;

        return Arrays.equals(days, that.days);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(days);
    }
}
