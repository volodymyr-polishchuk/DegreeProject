package app.data;

/**
 * Created by Vladimir on 17/02/18.
 **/
public interface DataModifyInterface {
    StudyData add();

    StudyData edit(StudyData t);

    boolean remove(StudyData t);

    void exit(StudyData[] t);
}
