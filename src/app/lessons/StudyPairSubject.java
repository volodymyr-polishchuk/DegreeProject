package app.lessons;

/**
 * Created by Vladimir on 29/01/18.
 **/
public class StudyPairSubject {
    private StudySubject numerator;
    private StudySubject denominator;
    public static final int NUMERATOR = 0;
    public static final int DENOMINATOR = 1;

    public StudyPairSubject(StudySubject numerator, StudySubject denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public StudyPairSubject(StudySubject subject) {
        this(subject, subject);
    }

    public static StudyPairSubject getEmptyInstance() {
        return new StudyPairSubject(new StudySubject(), new StudySubject());
    }

    public StudySubject getNumerator() {
        return numerator;
    }

    public StudySubject getByIndex(int index) {
        switch (index) {
            case NUMERATOR: return getNumerator();
            case DENOMINATOR: return getDenominator();
            default: throw new IllegalArgumentException("Argument must be constant NUMERATOR or DENOMINATOR");
        }
    }

    public StudySubject getDenominator() {
        if (numerator.equals(denominator)) {
            return new StudySubject();
        }
        return denominator;
    }


}
