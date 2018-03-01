package app.data;

/**
 * Created by Vladimir on 17/02/18.
 **/
public class Preference {
    private boolean Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;

    public Preference(boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday) {
        Monday = monday;
        Tuesday = tuesday;
        Wednesday = wednesday;
        Thursday = thursday;
        Friday = friday;
        Saturday = saturday;
    }

    public boolean getByIndex(int index) {
        switch (index) {
            case 0: return isMonday();
            case 1: return isTuesday();
            case 2: return isWednesday();
            case 3: return isThursday();
            case 4: return isFriday();
            case 5: return isSaturday();
            default: throw new IllegalArgumentException("Must be 0..5 (0 - Monday, 1 - Tuesday,..., 5 - Saturday), but get {" + index + "}");
        }
    }

    public boolean isMonday() {
        return Monday;
    }

    public void setMonday(boolean monday) {
        Monday = monday;
    }

    public boolean isTuesday() {
        return Tuesday;
    }

    public void setTuesday(boolean tuesday) {
        Tuesday = tuesday;
    }

    public boolean isWednesday() {
        return Wednesday;
    }

    public void setWednesday(boolean wednesday) {
        Wednesday = wednesday;
    }

    public boolean isThursday() {
        return Thursday;
    }

    public void setThursday(boolean thursday) {
        Thursday = thursday;
    }

    public boolean isFriday() {
        return Friday;
    }

    public void setFriday(boolean friday) {
        Friday = friday;
    }

    public boolean isSaturday() {
        return Saturday;
    }

    public void setSaturday(boolean saturday) {
        Saturday = saturday;
    }

    public String getData() {
        return String.valueOf(deParseChar(Monday)) + deParseChar(Tuesday) + deParseChar(Wednesday) +
                deParseChar(Thursday) + deParseChar(Friday) + deParseChar(Saturday);
    }

    public static Preference parsePreference(String line) {
        if (line.length() != 6) throw new IllegalArgumentException("Argument must be 6 char, but get: {" + line + "}");
        return new Preference(
                parseChar(line.charAt(0)),
                parseChar(line.charAt(1)),
                parseChar(line.charAt(2)),
                parseChar(line.charAt(3)),
                parseChar(line.charAt(4)),
                parseChar(line.charAt(5))
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
                "M=" + Monday +
                ", T=" + Tuesday +
                ", W=" + Wednesday +
                ", T=" + Thursday +
                ", F=" + Friday +
                ", S=" + Saturday +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Preference that = (Preference) o;

        if (Monday != that.Monday) return false;
        if (Tuesday != that.Tuesday) return false;
        if (Wednesday != that.Wednesday) return false;
        if (Thursday != that.Thursday) return false;
        if (Friday != that.Friday) return false;
        return Saturday == that.Saturday;

    }

    @Override
    public int hashCode() {
        int result = (Monday ? 1 : 0);
        result = 31 * result + (Tuesday ? 1 : 0);
        result = 31 * result + (Wednesday ? 1 : 0);
        result = 31 * result + (Thursday ? 1 : 0);
        result = 31 * result + (Friday ? 1 : 0);
        result = 31 * result + (Saturday ? 1 : 0);
        return result;
    }
}
