package app.data.loading;

import javax.swing.*;
import javax.swing.event.ListDataListener;

/**
 * Created by Vladimir on 24/05/18.
 **/
public class LoadingGroupListModel extends AbstractListModel<GroupLoad> {
    private SemesterLoad semesterLoad;

    public LoadingGroupListModel(SemesterLoad semesterLoad) {
        super();
        this.semesterLoad = semesterLoad;
    }

    @Override
    protected void fireContentsChanged(Object source, int index0, int index1) {
        super.fireContentsChanged(source, index0, index1);
    }

    public void fireDataChange() {
        fireContentsChanged(this, 0, getSize() - 1);
    }

    @Override
    public int getSize() {
        return semesterLoad.getGroupLoads().size();
    }

    @Override
    public GroupLoad getElementAt(int index) {
        return semesterLoad.getGroupLoads().get(index);
    }
}


