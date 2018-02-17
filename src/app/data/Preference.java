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
        return c == '0';
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
}
