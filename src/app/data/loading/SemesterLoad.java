package app.data.loading;

import app.DegreeProject;
import app.data.Group;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vladimir on 23/05/18.
 **/
public class SemesterLoad {
    private int key = -1;
    private static final boolean I_SEMESTER = true, II_SEMESTER = false;
    private static final int I_SEMESTER_NUMBER = 1, II_SEMESTER_NUMBER = 2;

    private int year;
    private boolean semester; // true - 1; false - 2
    private ArrayList<GroupLoad> groupLoads;

    public SemesterLoad() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        year = calendar.get(Calendar.YEAR);
        semester = calendar.get(Calendar.MONTH) > Calendar.JUNE;
        groupLoads = new ArrayList<>();
    }

    public String getPeriod() {
        return year + "-" + (year + 1) + "/" + (semester == I_SEMESTER ? I_SEMESTER_NUMBER : II_SEMESTER_NUMBER);
    }

    public void setPeriod(String line) {
        String[] lines = line.split("/");
        String[] years = lines[0].split("-");
        year = Integer.parseInt(years[0]);
        semester = lines[1].equals("1");
    }

    ArrayList<GroupLoad> getGroupLoads() {
        return groupLoads;
    }

    public void setGroupLoads(ArrayList<GroupLoad> groupLoads) {
        this.groupLoads = groupLoads;
    }

    public void add(Group group) {
        for (GroupLoad groupLoad : getGroupLoads()) {
            if (groupLoad.getGroup().equals(group)) {
                return;
            }
        }
        getGroupLoads().add(new GroupLoad(group));
    }

    String nextPeriod() {
        year++;
        return year + "-" + (year + 1);
    }

    String prevPeriod() {
        year--;
        return year + "-" + (year + 1);
    }

    public boolean isSemester() {
        return semester;
    }

    void setSemester(boolean semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean keyExists() {
        return key > -1;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void writeToDatabase() throws SQLException {
        boolean canWrite = true;
        for (GroupLoad item : groupLoads) {
            if (item.getGroup().keyExist()) {
                for (LoadUnit loadUnit : item.getLoadUnits()) {
                    if (loadUnit.getTeacher() == null && !loadUnit.getTeacher().keyExist()
                            && loadUnit.getLesson() == null && !loadUnit.getLesson().keyExist()) {
                        canWrite = false;
                    }
                }
            } else {
                canWrite = false;
            }
        }
        if (!canWrite) return;

        if (!keyExists()) {
            writeToDatabaseWhenKeyNotExists();
        } else {
            writeToDatabaseWhenKeyExists();
        }
    }

    private void writeToDatabaseWhenKeyExists() {

    }

    private void writeToDatabaseWhenKeyNotExists() throws SQLException {
        //Перевіряю чи немає за такий же період SemesterLoad
        String selectSemesterLoadWherePeriod = "SELECT * FROM semester_load WHERE period = ?";
        PreparedStatement preparedStatement = DegreeProject.databaseData.getConnection().prepareStatement(selectSemesterLoadWherePeriod);
        preparedStatement.setString(1, getPeriod());
        ResultSet resultSet = preparedStatement.executeQuery();
        //Якщо є, то я удаляю
        if (resultSet.next()) {
            int confirmResult = JOptionPane.showConfirmDialog(null, "Ви намагаєтеся зберегти навантаження за період\nякий присутній в базі даних. Перезаписати?", "Увага", JOptionPane.OK_CANCEL_OPTION);
            if (confirmResult == JOptionPane.YES_OPTION)
                deleteSemesterLoadFromDatabase(resultSet.getInt("semester_load.k"));
            else
                return;
        }
        resultSet.close();
        preparedStatement.close();
        //Добавляю SemesterLoad
        String writeSemesterLoadSQL = "INSERT INTO semester_load(period) VALUES (?)";
        PreparedStatement writeSemesterLoadPreparedStatement =
                DegreeProject.databaseData.getConnection().prepareStatement(writeSemesterLoadSQL, Statement.RETURN_GENERATED_KEYS);
        writeSemesterLoadPreparedStatement.setString(1, getPeriod());
        writeSemesterLoadPreparedStatement.executeUpdate();
        ResultSet semesterLoadKeys = writeSemesterLoadPreparedStatement.getGeneratedKeys();
        if (semesterLoadKeys.next())
            setKey(semesterLoadKeys.getInt(1));
        writeSemesterLoadPreparedStatement.close();

        String writeGroupLoadSQL = "INSERT INTO group_load(group_key, semester_load_k, week_count) VALUES (?, ?, ?)";
        PreparedStatement writeGroupLoadPreparedStatement =
                DegreeProject.databaseData.getConnection().prepareStatement(writeGroupLoadSQL, Statement.RETURN_GENERATED_KEYS);
        String writeLoadUnitsSQL = "INSERT INTO load_unit(sorted_number, total_amount, lecture, practical, laboratory, control_form, lesson, teacher, group_load_k) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement writeLoadUnitsPS =
                DegreeProject.databaseData.getConnection().prepareStatement(writeLoadUnitsSQL, Statement.RETURN_GENERATED_KEYS);
        //Пишу GroupLoad
        for (GroupLoad groupLoad : groupLoads) {
            writeGroupLoadPreparedStatement.setInt(1, groupLoad.getGroup().getKey());
            writeGroupLoadPreparedStatement.setInt(2, getKey());
            writeGroupLoadPreparedStatement.setInt(3, groupLoad.getWeekCount());
            writeGroupLoadPreparedStatement.executeUpdate();
            ResultSet groupLoadKeys = writeGroupLoadPreparedStatement.getGeneratedKeys();
            if (groupLoadKeys.next())
                groupLoad.setKey(groupLoadKeys.getInt(1));

            //Пишу LoadUnit
            ArrayList<LoadUnit> loadUnits = groupLoad.getLoadUnits();
            for (int i = 0; i < loadUnits.size(); i++) {
                LoadUnit loadUnit = loadUnits.get(i);
                writeLoadUnitsPS.setInt(1, i);
                writeLoadUnitsPS.setInt(2, loadUnit.getTotalAmount());
                writeLoadUnitsPS.setInt(3, loadUnit.getLectures());
                writeLoadUnitsPS.setInt(4, loadUnit.getPractical());
                writeLoadUnitsPS.setInt(5, loadUnit.getLaboratory());
                writeLoadUnitsPS.setString(6, loadUnit.getControlForm());
                writeLoadUnitsPS.setInt(7, loadUnit.getLesson().getKey());
                writeLoadUnitsPS.setInt(8, loadUnit.getTeacher().getKey());
                writeLoadUnitsPS.setInt(9, groupLoad.getKey());
                writeLoadUnitsPS.executeUpdate();
                ResultSet loadUnitsKeys = writeLoadUnitsPS.getGeneratedKeys();
                if (loadUnitsKeys.next())
                    loadUnit.setKey(loadUnitsKeys.getInt(1));
            }
        }
        writeLoadUnitsPS.close();
        writeGroupLoadPreparedStatement.close();

    }

    private void deleteSemesterLoadFromDatabase(int key) throws SQLException {
        String deleteSemesterLoadSQL = "DELETE FROM semester_load WHERE k LIKE ?";
        PreparedStatement preparedStatement = DegreeProject.databaseData.getConnection().prepareStatement(deleteSemesterLoadSQL);
        preparedStatement.setInt(1, key);
        preparedStatement.execute();
        preparedStatement.closeOnCompletion();
    }

}
