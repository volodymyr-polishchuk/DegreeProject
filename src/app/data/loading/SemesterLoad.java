package app.data.loading;

import app.DegreeProject;
import app.data.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
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

    SemesterLoad() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        year = calendar.get(Calendar.YEAR);
        semester = calendar.get(Calendar.MONTH) > Calendar.JUNE;
        groupLoads = new ArrayList<>();
    }

    public SemesterLoad(int key) {
        this();
        try {
            readSemesterLoadFromDatabase(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void readSemesterLoadFromDatabase(int key) throws SQLException {
        Connection connection = DegreeProject.databaseData.getConnection();
        setKey(key);
        String readSemesterLoadSQL = "SELECT * FROM semester_load WHERE k LIKE ?";
        PreparedStatement readSemesterLoadStatement = connection.prepareStatement(readSemesterLoadSQL);
        readSemesterLoadStatement.setInt(1, key);
        ResultSet readSemesterLoadResultSet = readSemesterLoadStatement.executeQuery();
        if (readSemesterLoadResultSet.next())
            setPeriod(readSemesterLoadResultSet.getString("period"));
        readSemesterLoadResultSet.close();
        readSemesterLoadStatement.close();

        String readGroupLoadSQL = "SELECT * FROM group_load INNER JOIN groups ON group_load.group_key = groups.k INNER JOIN departments ON groups.department = departments.k  WHERE semester_load_k LIKE ?";
        PreparedStatement readGroupLoadStatement = connection.prepareStatement(readGroupLoadSQL);
        readGroupLoadStatement.setInt(1, getKey());
        ResultSet readGroupLoadRS = readGroupLoadStatement.executeQuery();

        String readLoadUnitSQL = "SELECT * FROM load_unit INNER JOIN lessons ON load_unit.lesson = lessons.k INNER JOIN teachers ON load_unit.teacher = teachers.k INNER JOIN auditorys ON lessons.auditory = auditorys.k WHERE group_load_k = ?";
        PreparedStatement readLoadUnitsStatement = connection.prepareStatement(readLoadUnitSQL);
        while (readGroupLoadRS.next()) {
            GroupLoad groupLoad = new GroupLoad(new Group(readGroupLoadRS.getInt("groups.k"),
                            new Department(readGroupLoadRS.getInt("departments.k"), readGroupLoadRS.getString("departments.name")),
                            readGroupLoadRS.getString("groups.name")));
            groupLoad.setKey(readGroupLoadRS.getInt("group_load.k"));
            groupLoad.setWeekCount(readGroupLoadRS.getInt("group_load.week_count"));
            readLoadUnitsStatement.setInt(1, groupLoad.getKey());
            ResultSet readLURS = readLoadUnitsStatement.executeQuery();
            while (readLURS.next()) {
                LoadUnit loadUnitItem = new LoadUnit(groupLoad,
                        new Lesson(readLURS.getInt("lessons.k"),
                                readLURS.getString("lessons.name"),
                                new Auditory(readLURS.getInt("auditorys.k"), readLURS.getString("auditorys.name"))),
                        new Teacher(readLURS.getInt("teachers.k"), readLURS.getString("teachers.name"), Preference.parsePreference(readLURS.getString("teachers.preferences"))),
                        readLURS.getInt("total_amount"),
                        readLURS.getInt("lecture"),
                        readLURS.getInt("practical"),
                        readLURS.getInt("laboratory"),
                        readLURS.getString("control_form")
                );
                loadUnitItem.setKey(readLURS.getInt("k"));
                groupLoad.getLoadUnits().add(readLURS.getInt("sorted_number"), loadUnitItem);
            }
            groupLoads.add(groupLoad);
        }
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

    public ArrayList<GroupLoad> getGroupLoads() {
        return groupLoads;
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

    int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    private boolean keyExists() {
        return key > -1;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    void writeToDatabase() throws SQLException {
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

    private void writeToDatabaseWhenKeyExists() throws SQLException {
        String writeSemesterLoadSQL = "INSERT INTO semester_load(k, period) VALUE (?, ?) ON DUPLICATE KEY UPDATE period = ?";
        PreparedStatement writeSemesterLoadStatement = DegreeProject.databaseData.getConnection().prepareStatement(writeSemesterLoadSQL);
        writeSemesterLoadStatement.setInt(1, getKey());
        writeSemesterLoadStatement.setString(2, getPeriod());
        writeSemesterLoadStatement.setString(3, getPeriod());
        writeSemesterLoadStatement.execute();

        String deleteAllGroupLoadSQL = "DELETE FROM group_load WHERE semester_load_k = ?";
        PreparedStatement deleteAllGroupLoadStatement = DegreeProject.databaseData.getConnection().prepareStatement(deleteAllGroupLoadSQL);
        deleteAllGroupLoadStatement.setInt(1, getKey());
        deleteAllGroupLoadStatement.execute();

        groupLoadWriter();
    }

    private void writeToDatabaseWhenKeyNotExists() throws SQLException {
        //Перевіряю чи немає за такий же період SemesterLoad
        String selectSemesterLoadWherePeriod = "SELECT * FROM semester_load WHERE period = ?";
        PreparedStatement preparedStatement = DegreeProject.databaseData.getConnection().prepareStatement(selectSemesterLoadWherePeriod);
        preparedStatement.setString(1, getPeriod());
        ResultSet resultSet = preparedStatement.executeQuery();
        //Якщо є, то я удаляю
        if (resultSet.next()) {
            int confirmResult = JOptionPane.showConfirmDialog(null,
                    "Ви намагаєтеся зберегти навантаження за період" +
                            "\nякий присутній в базі даних. Перезаписати?",
                    "Увага", JOptionPane.OK_CANCEL_OPTION);
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

        groupLoadWriter();

    }

    private void groupLoadWriter() throws SQLException {
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

    void exportToExcel(File file) throws IOException {
        final int GROUP_COLUMN_START = 0, GROUP_COLUMN_END = 2, WEEK_COUNT_COLUMN_START = 3, WEEK_COUNT_COLUMN_END = 4,
            PERIOD_COLUMN_START = 5, PERIOD_COLUMN_END = 9;
        final int NUMBER_COLUMN = 0, LESSON_COLUMN = 1, TEACHER_COLUMN = 2, TOTAL_AMOUNT_COLUMN = 3, AUDITORY_COLUMN = 4,
            LECTURE_COLUMN = 5, PRACTICAL_COLUMN = 6, LABORATORY_COLUMN = 7, LOAD_COLUMN = 8, CONTROL_FORM_COLUMN = 9;

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Навантаження на " + (getSemester() ? "I" : "II") + " семестер " + getYear() + "-" + (getYear() + 1) + " н. р.");

        int rowCounter = 0;
        for (GroupLoad groupLoad : groupLoads) {
            HSSFRow subscribeRow = sheet.createRow(rowCounter);

            HSSFCell groupNameCell = subscribeRow.createCell(GROUP_COLUMN_START);
            groupNameCell.setCellValue("Група " + groupLoad.getGroup().getName() + "; Відділення " + groupLoad.getGroup().getDepartment().getName());
            sheet.addMergedRegion(new CellRangeAddress(rowCounter, rowCounter, GROUP_COLUMN_START, GROUP_COLUMN_END));

            HSSFCell weekCountCell = subscribeRow.createCell(WEEK_COUNT_COLUMN_START);
            weekCountCell.setCellValue("(" + groupLoad.getWeekCount() + " тижнів)");
            sheet.addMergedRegion(new CellRangeAddress(rowCounter, rowCounter, WEEK_COUNT_COLUMN_START, WEEK_COUNT_COLUMN_END));

            HSSFCell periodCell = subscribeRow.createCell(PERIOD_COLUMN_START);
            periodCell.setCellValue((getSemester() ? "I" : "II") + " семестер " + getYear() + "-" + (getYear() + 1) + " н. р.");
            sheet.addMergedRegion(new CellRangeAddress(rowCounter, rowCounter, PERIOD_COLUMN_START, PERIOD_COLUMN_END));

            rowCounter++;

            HSSFRow headerRow = sheet.createRow(rowCounter);
            headerRow.createCell(NUMBER_COLUMN).setCellValue("№ п/п");
            headerRow.createCell(LESSON_COLUMN).setCellValue("Назва дисципліни");
            headerRow.createCell(TEACHER_COLUMN).setCellValue("Викладач");
            headerRow.createCell(TOTAL_AMOUNT_COLUMN).setCellValue("Загальний обсяг");
            headerRow.createCell(AUDITORY_COLUMN).setCellValue("Аудиторних");
            headerRow.createCell(LECTURE_COLUMN).setCellValue("Лекцій");
            headerRow.createCell(PRACTICAL_COLUMN).setCellValue("Практичних");
            headerRow.createCell(LABORATORY_COLUMN).setCellValue("Лабораторних");
            headerRow.createCell(LOAD_COLUMN).setCellValue("Навантаження");
            headerRow.createCell(CONTROL_FORM_COLUMN).setCellValue("Форма контролю");

            rowCounter++;

            ArrayList<LoadUnit> loadUnits = groupLoad.getLoadUnits();
            for (int i = 0; i < loadUnits.size(); i++) {
                LoadUnit loadUnit = loadUnits.get(i);
                HSSFRow dataRow = sheet.createRow(rowCounter);
                dataRow.createCell(NUMBER_COLUMN).setCellValue(i + 1);
                dataRow.createCell(LESSON_COLUMN).setCellValue(loadUnit.getLesson().getName());
                dataRow.createCell(TEACHER_COLUMN).setCellValue(loadUnit.getTeacher().getName());
                dataRow.createCell(TOTAL_AMOUNT_COLUMN).setCellValue(loadUnit.getTotalAmount());
                dataRow.createCell(AUDITORY_COLUMN).setCellValue(loadUnit.getClassroom());
                dataRow.createCell(LECTURE_COLUMN).setCellValue(loadUnit.getLectures());
                dataRow.createCell(PRACTICAL_COLUMN).setCellValue(loadUnit.getPractical());
                dataRow.createCell(LABORATORY_COLUMN).setCellValue(loadUnit.getLaboratory());
                dataRow.createCell(LOAD_COLUMN).setCellValue(Math.round(loadUnit.getWeekLoad() * 100) / 100.0);
                dataRow.createCell(CONTROL_FORM_COLUMN).setCellValue(loadUnit.getControlForm());
                rowCounter++;
            }

            sheet.createRow(rowCounter).createCell(LOAD_COLUMN).setCellValue(Math.round(groupLoad.getWeekLoadingSum() * 100) / 100.0);
            rowCounter++;
            rowCounter++;
        }

        workbook.write(file);
    }

    public GroupLoad getGroupLoadByGroup(Group group) {
        for (GroupLoad groupLoad : groupLoads) {
            if (groupLoad.getGroup().equals(group)) return groupLoad;
        }
        return null;
    }

    boolean getSemester() {
        return semester;
    }
}
