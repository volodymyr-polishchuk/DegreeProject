package app.data.loading;

import app.data.Group;
import app.data.Lesson;
import app.data.Teacher;

import javax.swing.table.DefaultTableModel;

/**
 * Created by Vladimir on 23/05/18.
 **/
public class LoadingTableModel extends DefaultTableModel {
    public static final int COLUMN_COUNT = 10;
    public static final int
        NUMBER_COLUMN = 0, LESSON_COLUMN = 1, TEACHER_COLUMN = 2, AMOUNT_LOADING_COLUMN = 3, AUDITORY_COLUMN = 4,
            LECTURE_COLUMN = 5, PRACTICAL_COLUMN = 6, LABORATORY_COLUMN = 7, WEEK_LOADING_COLUMN = 8, CONTROL_FORM_COLUMN = 9;

    private static final String[] COLUMN_NAMES = new String[]
            {"№", "Предмет", "Викладач", "Загальне", "Аудиторні", "Лекцій", "Практичні", "Лабораторні", "Тиждневе", "Контроль"};

    private GroupLoad groupLoad;

    public LoadingTableModel() {

    }

    public LoadingTableModel(Group group) {
        groupLoad = new GroupLoad(group);
    }

    public LoadingTableModel(GroupLoad groupLoad) {
        this.groupLoad = groupLoad;
    }

    GroupLoad getGroupLoad() {
        return groupLoad;
    }

    void setGroupLoad(GroupLoad groupLoad) {
        this.groupLoad = groupLoad;
    }

    void addNewRecord() {
        if (groupLoad != null) {
            groupLoad.getLoadUnits().add(new LoadUnit(groupLoad));
        }
    }

    boolean moveRecordDown(int index) {
        if (index >= 0 && index < groupLoad.getLoadUnits().size() - 1 && groupLoad != null) {
            groupLoad.getLoadUnits().add(index + 1, groupLoad.getLoadUnits().remove(index));
            return true;
        } else {
            return false;
        }
    }

    boolean moveRecordUp(int index) {
        if (index > 0 && index < groupLoad.getLoadUnits().size() && groupLoad != null) {
            groupLoad.getLoadUnits().add(index - 1, groupLoad.getLoadUnits().remove(index));
            return true;
        } else {
            return false;
        }
    }

    LoadUnit removeRecord(int index) {
        if (index >= 0 && index < groupLoad.getLoadUnits().size() && groupLoad != null)
            return groupLoad.getLoadUnits().remove(index);
        else
            return null;
    }

    @Override
    public int getRowCount() {
        if (groupLoad == null) return 0;
        return groupLoad.getLoadUnits().size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case NUMBER_COLUMN:case AMOUNT_LOADING_COLUMN:case LECTURE_COLUMN:case PRACTICAL_COLUMN:
            case LABORATORY_COLUMN:case AUDITORY_COLUMN:case WEEK_LOADING_COLUMN:
                return Integer.class;
            case CONTROL_FORM_COLUMN:
                return String.class;
            case LESSON_COLUMN:
                return Lesson.class;
            case TEACHER_COLUMN:
                return Teacher.class;
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case NUMBER_COLUMN:case AUDITORY_COLUMN:case WEEK_LOADING_COLUMN:
                return false;
        }
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case NUMBER_COLUMN:         return rowIndex + 1;
            case LESSON_COLUMN:         return groupLoad.getLoadUnits().get(rowIndex).getLesson();
            case TEACHER_COLUMN:        return groupLoad.getLoadUnits().get(rowIndex).getTeacher();
            case AMOUNT_LOADING_COLUMN: return groupLoad.getLoadUnits().get(rowIndex).getTotalAmount();
            case AUDITORY_COLUMN:       return groupLoad.getLoadUnits().get(rowIndex).getClassroom();
            case LECTURE_COLUMN:        return groupLoad.getLoadUnits().get(rowIndex).getLectures();
            case PRACTICAL_COLUMN:      return groupLoad.getLoadUnits().get(rowIndex).getPractical();
            case LABORATORY_COLUMN:     return groupLoad.getLoadUnits().get(rowIndex).getLaboratory();
            case WEEK_LOADING_COLUMN:   return groupLoad.getLoadUnits().get(rowIndex).getWeekLoad();
            case CONTROL_FORM_COLUMN:   return groupLoad.getLoadUnits().get(rowIndex).getControlForm();
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex >= groupLoad.getLoadUnits().size()) return;
        LoadUnit unit = groupLoad.getLoadUnits().get(rowIndex);
        switch (columnIndex) {
            case LESSON_COLUMN:
                if (aValue instanceof Lesson)
                    unit.setLesson((Lesson)aValue);
                break;
            case TEACHER_COLUMN:
                if (aValue instanceof Teacher)
                    unit.setTeacher((Teacher)aValue);
                break;
            case AMOUNT_LOADING_COLUMN:
                if (aValue instanceof Integer)
                    unit.setTotalAmount((int)aValue);
                break;
            case LECTURE_COLUMN:
                if (aValue instanceof Integer)
                    unit.setLectures((int)aValue);
                break;
            case PRACTICAL_COLUMN:
                if (aValue instanceof Integer)
                    unit.setPractical((int)aValue);
                break;
            case LABORATORY_COLUMN:
                if (aValue instanceof Integer)
                    unit.setLaboratory((int)aValue);
                break;
            case CONTROL_FORM_COLUMN:
                if (aValue instanceof String)
                    unit.setControlForm((String)aValue);
                break;
        }
        fireTableDataChanged();
    }
}
