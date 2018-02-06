package app.DeprecatedLessons;

/**
 * Created by Vladimir on 30/01/18.
 **/
public class StudyPairSubject {

    private StudySubject numerator;
    private StudySubject denominator;
    private PairType type;

    public StudyPairSubject() {
        numerator = new StudySubject();
        denominator = new StudySubject();
        type = PairType.BOTH;
    }

    public void setSubject(PairType how, StudySubject subject) {
        switch (how) {
            case NUMERATOR: numerator = subject;
                break;
            case DENOMINATOR: denominator = subject;
                break;
            case BOTH:
                numerator = subject;
                denominator = subject;
                break;
        }
        type = how;
    }

    public StudySubject getStudySubject(PairType how) {
        switch (how) {
            case NUMERATOR: return numerator;
            case DENOMINATOR: return denominator;
            default: return numerator;
        }
    }

    public PairType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyPairSubject that = (StudyPairSubject) o;

        if (numerator != null ? !numerator.equals(that.numerator) : that.numerator != null) return false;
        if (denominator != null ? !denominator.equals(that.denominator) : that.denominator != null) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = numerator != null ? numerator.hashCode() : 0;
        result = 31 * result + (denominator != null ? denominator.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StudyPairSubject{" +
                "numerator=" + numerator +
                ", denominator=" + denominator +
                ", type=" + type +
                '}';
    }
}
