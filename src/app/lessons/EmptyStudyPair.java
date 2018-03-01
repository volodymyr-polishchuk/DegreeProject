package app.lessons;

import javax.swing.*;
import java.util.List;

/**
 * Created by Vladimir on 06/02/18.
 **/
public class EmptyStudyPair extends StudyPair {

    @Override
    public JComponent getRendererComponent(Query data) {
        JLabel jLabel = new JLabel("");
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return jLabel;
    }

    @Override
    public Forbidden[] getForbidden(StudyPair studyPair) {
        return new Forbidden[0];
    }

    @Override
    public Forbidden[] getForbidden(StudyPair studyPair, List<LessonsUnit> units, int row, int col, int pairPerDay, int dayPerWeek) {
        return getForbidden(studyPair);
    }

    @Override
    public Forbidden[] getSelfForbidden(int row, int col, int pairPerDay, int dayPerWeek) {
        return new Forbidden[0];
    }


}
