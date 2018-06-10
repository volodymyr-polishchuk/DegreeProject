package app;

import app.data.*;
import app.data.loading.SemesterLoad;
import app.frame.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeSet;

import static javax.swing.JOptionPane.YES_OPTION;

/**
 * Created by Vladimir on 03/03/18.
 **/
public class MainFormMenuBar extends JMenuBar {
    private MainForm mainForm;

    public MainFormMenuBar(MainForm mainForm) {
        this.mainForm = mainForm;
        //Створення головного меню програми
//          Створення пункту меню ПРОГРАМА
        JMenu programMenu = new JMenu("Програма");
        programMenu.add(new JMenuItem("Виконати підключення до іншого сервера")).addActionListener(this::MenuItemReconnect);
        programMenu.add(new JMenuItem("Налаштування")).addActionListener(this::MenuItemSetting);
        programMenu.add(new JPopupMenu.Separator());
        programMenu.add(new JMenuItem("Консоль")).addActionListener(e -> new SQLConsole(DegreeProject.databaseData.getConnection()).setVisible(true));
        programMenu.add(new JPopupMenu.Separator());
        programMenu.add(new JMenuItem("Вихід")).addActionListener(this::MenuItemExit);
        add(programMenu);

//          Створення меню Навчальний графік
        JMenu scheduleMenu = new JMenu("Навчальний графік");
        scheduleMenu.add(new JMenuItem("Створити навчальний графік")).addActionListener(this::MenuItemCreateSchedule);
        scheduleMenu.add(new JPopupMenu.Separator());
        scheduleMenu.add(new JMenuItem("Переглянути/редагувати графік")).addActionListener(this::MenuItemViewSchedule);
        scheduleMenu.add(new JMenuItem("Видалити навчальний графік")).addActionListener(this::MenuItemRemoveSchedule);
        add(scheduleMenu);

//          Створення меню Розклад занять
        JMenu lessonsMenu = new JMenu("Розклад занять");
        lessonsMenu.add(new JMenuItem("Створити розклад занять")).addActionListener(this::MenuItemCreateLessons);
        lessonsMenu.add(new JMenuItem("Створити розклад занять з навантаженням")).addActionListener(this::MenuItemCreateLessonsWithLoad);
        lessonsMenu.add(new JPopupMenu.Separator());
        lessonsMenu.add(new JMenuItem("Переглянути/редагувати розклад")).addActionListener(this::MenuItemViewLessons);
        lessonsMenu.add(new JMenuItem("Видалити розклад занять")).addActionListener(this::MenuItemRemoveLessons);
        add(lessonsMenu);

        JMenu semesterLoadingMenu = new JMenu("Навантаження");
        semesterLoadingMenu.add(new JMenuItem("Створити навантаження")).addActionListener(this::MenuItemCreateSemesterLoading);
        semesterLoadingMenu.add(new JPopupMenu.Separator());
        semesterLoadingMenu.add(new JMenuItem("Переглянути/редагувати навантаження")).addActionListener(this::MenuItemViewSemesterLoading);
        semesterLoadingMenu.add(new JMenuItem("Видалити навантаження")).addActionListener(this::MenuItemRemoveSemesterLoading);
        add(semesterLoadingMenu);

//          Створення меню Дані
        JMenu dataMenu = new JMenu("Дані");
        dataMenu.add(new JMenuItem("Аудиторії")).addActionListener(this::MenuItemDataAuditory);
        dataMenu.add(new JMenuItem("Викладачі")).addActionListener(this::MenuItemDataTeacher);
        dataMenu.add(new JMenuItem("Предмети")).addActionListener(this::MenuItemDataLesson);
        dataMenu.add(new JPopupMenu.Separator());
        dataMenu.add(new JMenuItem("Групи")).addActionListener(this::MenuItemDataGroup);
        dataMenu.add(new JPopupMenu.Separator());
        dataMenu.add(new JMenuItem("Редагувати дні")).addActionListener(this::MenuItemDataHoliday);

        add(dataMenu);

//          Створення меню Довідка
        JMenu helpMenu = new JMenu("Довідка");
        helpMenu.add(new JMenuItem("Допомога користувачеві")).addActionListener(this::MenuItemHelp);
        helpMenu.add(new JMenuItem("Перевірка оновлень")).addActionListener(this::MenuItemCheckUpdate);
        JMenuItem MenuItemAbout = new JMenuItem("Про програму");
        helpMenu.add(MenuItemAbout).addActionListener(this::MenuItemAbout);
        add(helpMenu);
    }

    private void MenuItemCreateLessonsWithLoad(ActionEvent event) {
        LoadChoiceDialog dialog = new LoadChoiceDialog(DegreeProject.databaseData.getConnection()) {
            @Override
            protected void onOK() {
                ListItem item = getJList().getSelectedValue();
                DegreeProject.mainForm.addTab(new LessonsPanel("Розклад занять за навантаженням", new SemesterLoad(item.getKey())), "Розклад занять за навантаженням");
                dispose();
            }
        };
        dialog.setVisible(true);
    }

    private void MenuItemRemoveSemesterLoading(ActionEvent event) {
        LoadRemoveDialog removeDialog = new LoadRemoveDialog(DegreeProject.databaseData.getConnection());
        removeDialog.setVisible(true);
    }

    private void MenuItemViewSemesterLoading(ActionEvent event) {
        LoadChoiceDialog dialog = new LoadChoiceDialog(DegreeProject.databaseData.getConnection());
        dialog.setVisible(true);
    }

    private void MenuItemCreateSemesterLoading(ActionEvent event) {
        this.mainForm.addEmptySemesterLoading();
    }

    public void MenuItemDataHoliday(ActionEvent event) {
        try (Statement st = DegreeProject.databaseData.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM holidays")
        ) {
            TreeSet<Holiday> groupTreeSet = new TreeSet<>();
            while (rs.next()) {
                groupTreeSet.add(new Holiday(rs.getDate("date"), rs.getBoolean("repeats"), rs.getInt("k")));
            }
            Holiday[] inputData = new Holiday[groupTreeSet.size()];
            int count = 0;
            for (Holiday holiday : groupTreeSet) inputData[count++] = holiday;
            StudyData[] outputData = DataModifyDialog.getInstance(inputData, new DataModifyInterface() {
                public StudyData add() {
                    return HolidayDialogModify.getModify();
                }
                public StudyData edit(StudyData t) {
                    return HolidayDialogModify.getModify((Holiday) t);
                }
                public boolean remove(StudyData t) {
                    return true;
                }
                public void exit(StudyData[] t) {}
            }, "Налаштування днів");

            boolean b = true;
            if (outputData.length == inputData.length)
                for (int i = 0; i < outputData.length; i++) {
                    Holiday in = inputData[i];
                    Holiday out = (Holiday) outputData[i];
                    if (!in.equals(out)) {
                        b = false;
                        break;
                    }
                }
            else b = false;
            if (b) return;

            for (Holiday in : inputData) {
                boolean bool = true;
                for (StudyData out : outputData) {
                    if (in.equals(out)) {
                        bool = false;
                        break;
                    }
                }
                if (bool) st.execute("DELETE FROM holidays WHERE k LIKE '" + in.getKey() + "'");
            }
            PreparedStatement preparedStatement = DegreeProject.databaseData.getConnection().prepareStatement(
                    "INSERT INTO holidays(date, repeats, k) VALUE (?, ?, ?) ON DUPLICATE KEY UPDATE date = ?, repeats = ?");
            PreparedStatement preparedStatementElse = DegreeProject.databaseData.getConnection().prepareStatement(
                    "INSERT INTO holidays(date, repeats) VALUE (?, ?)");
            for (StudyData item : outputData) {
                Holiday holiday = (Holiday)item;
                if (item.keyExist()) {
                    preparedStatement.setDate(1, new java.sql.Date(holiday.getDate().getTime()));
                    preparedStatement.setBoolean(2, holiday.getRepeat());
                    preparedStatement.setInt(3, holiday.getKey());
                    preparedStatement.setDate(4, new java.sql.Date(holiday.getDate().getTime()));
                    preparedStatement.setBoolean(5, holiday.getRepeat());
                    preparedStatement.execute();
                } else {
                    preparedStatementElse.setDate(1, new java.sql.Date(holiday.getDate().getTime()));
                    preparedStatementElse.setBoolean(2, holiday.getRepeat());
                    preparedStatementElse.execute();
                }
            }
            preparedStatement.close();
            preparedStatementElse.close();
            DegreeProject.mainForm.setStatusBar("Дані успішно збережено до бази даних");
            JOptionPane.showMessageDialog(null, "Дані успішно змінено");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    public void MenuItemHelp(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://volodymyr-polishchuk.github.io/DegreeProject/"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void MenuItemAbout(ActionEvent event) {
        new AboutFrame().setVisible(true);
    }

    public void MenuItemCheckUpdate(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/volodymyr-polishchuk/DegreeProject"));
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

    public void MenuItemRemoveLessons(ActionEvent event) {
        LessonRemoveDialog removeDialog = new LessonRemoveDialog(DegreeProject.databaseData.getConnection());
        removeDialog.setVisible(true);
    }

    public void MenuItemViewLessons(ActionEvent event) {
        LessonChoiceDialog dialog = new LessonChoiceDialog(DegreeProject.databaseData.getConnection());
        dialog.setVisible(true);
    }

    public void MenuItemSetting(ActionEvent event) {
        (new SettingForm()).setVisible(true);
    }

    public void MenuItemDataGroup(ActionEvent event) {
        try (Statement st = DegreeProject.databaseData.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM groups INNER JOIN departments ON groups.department = departments.k ORDER BY groups.name")
        ) {
            TreeSet<Group> groupTreeSet = new TreeSet<>();
            while (rs.next()) {
                groupTreeSet.add(
                        new Group(
                                rs.getInt("groups.k"),
                                new Department(rs.getInt("departments.k"), rs.getString("departments.name")),
                                rs.getString("groups.name"),
                                rs.getString("groups.comments")));
            }
            Group[] inputData = new Group[groupTreeSet.size()];
            int count = 0;
            for (Group group : groupTreeSet) inputData[count++] = group;
            StudyData[] outputData = DataModifyDialog.getInstance(inputData, new DataModifyInterface() {
                public StudyData add() {
                    return GroupDialogModify.getModify();
                }
                public StudyData edit(StudyData t) {
                    return GroupDialogModify.getModify((Group) t);
                }
                public boolean remove(StudyData t) {
                    return true;
                }
                public void exit(StudyData[] t) {}
            }, "Налаштування груп");

            boolean b = true;
            if (outputData.length == inputData.length)
                for (int i = 0; i < outputData.length; i++) {
                    Group in = inputData[i];
                    Group out = (Group) outputData[i];
                    if (!in.equals(out)) {
                        b = false;
                        break;
                    }
                }
            else b = false;
            if (b) return;

            for (Group in : inputData) {
                boolean bool = true;
                for (StudyData out : outputData) {
                    if (/*in.equals(out)*/in.getKey() == out.getKey() || !out.keyExist()) {
                        bool = false;
                        break;
                    }
                }
                if (bool) st.execute("DELETE FROM groups WHERE k LIKE '" + in.getKey() + "'");
            }
            PreparedStatement preparedStatement = DegreeProject.databaseData.getConnection().prepareStatement(
                    "INSERT INTO groups(name, department, dateofcreate, k, comments) VALUE (?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE name = ?, department = ?, dateofcreate = ?, comments = ?");
            PreparedStatement preparedStatementElse = DegreeProject.databaseData.getConnection().prepareStatement(
                    "INSERT INTO groups(name, department, dateofcreate, comments) VALUE (?, ?, ?, ?)");
            for (StudyData item : outputData) {
                if (item.keyExist()) {
                    preparedStatement.setString(1, item.getName());
                    preparedStatement.setInt(2, ((Group) (item)).getDepartment().getKey());
                    preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                    preparedStatement.setInt(4, item.getKey());
                    preparedStatement.setString(5, ((Group) item).getComments());
                    preparedStatement.setString(6, item.getName());
                    preparedStatement.setInt(7, ((Group) (item)).getDepartment().getKey());
                    preparedStatement.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                    preparedStatement.setString(9, ((Group) item).getComments());
                    preparedStatement.execute();
                } else {
                    preparedStatementElse.setString(1, item.getName());
                    preparedStatementElse.setInt(2, ((Group) item).getDepartment().getKey());
                    preparedStatementElse.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                    preparedStatementElse.setString(4, ((Group) item).getComments());
                    preparedStatementElse.execute();
                }
            }
            preparedStatement.close();
            preparedStatementElse.close();
            DegreeProject.mainForm.setStatusBar("Дані успішно збережено до бази даних");
            JOptionPane.showMessageDialog(null, "Дані успішно змінено");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    public void MenuItemDataLesson(ActionEvent event) {
        try (Statement st = DegreeProject.databaseData.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM lessons INNER JOIN auditorys ON lessons.auditory = auditorys.k ORDER BY lessons.name")
        ) {
            ArrayList<Lesson> inputArray = new ArrayList<>();
            while (rs.next()) {
                inputArray.add(new Lesson(rs.getInt("k"), rs.getString("name"), new Auditory(rs.getInt("auditorys.k"), rs.getString("auditorys.name"))));
            }
            Lesson[] inputData = new Lesson[inputArray.size()];
            inputData = inputArray.toArray(inputData);
//                Ключі для аудиторії задаються не тільки тут, а й LessonsModify, тому при зміні структури ключа варто змінити і там
            StudyData[] outputData = DataModifyDialog.getInstance(inputData, new DataModifyInterface() {
                public StudyData add() {
                    return LessonDialogModify.getModify();
                }
                public StudyData edit(StudyData t) {
                    return LessonDialogModify.getModify((Lesson) t);
                }
                public boolean remove(StudyData t) {
                    return true;
                }
                public void exit(StudyData[] t) {}
            }, "Налаштування предметів");

//          Перевірка чи щось змінилося
            boolean b = true;
            if (outputData.length == inputData.length) {
                for (int i = 0; i < outputData.length; i++) {
                    Lesson in = inputData[i];
                    Lesson out = (Lesson) outputData[i];
                    if (!in.equals(out)) {
                        b = false;
                        break;
                    }
                }
            } else b = false;
//          Якщо нічого не змінилося, тоді виходимо з методу
            if (b) return;
//          Якщо всетаки змінилося тоді вибираємо тих що треба видалити
            for (Lesson in : inputData) {
                boolean isLessonDelete = true;
                for (StudyData out : outputData) {
                    if (/*in.equals(out)*/ in.getKey() == out.getKey() || !out.keyExist()) {
                        isLessonDelete = false;
                        break;
                    }
                }
                if (isLessonDelete) st.execute("DELETE FROM lessons WHERE k LIKE '" + in.getKey() + "'");
            }

            for (StudyData item : outputData) {
                if (item.keyExist())
                    st.execute("INSERT INTO lessons(name, auditory, k) VALUE ('" + item.getName() + "', '"
                            + ((Lesson) item).getAuditory().getKey() + "', " + item.getKey() + ") " +
                            "ON DUPLICATE KEY UPDATE name = '" + item.getName() + "', auditory = '" + ((Lesson) item).getAuditory().getKey() + "'");
                else
                    st.execute("INSERT INTO lessons(name, auditory) VALUE ('" + item.getName() + "', '" + ((Lesson) item).getAuditory().getKey() + "');");
            }
            DegreeProject.mainForm.setStatusBar("Дані успішно збережено до бази даних");
            JOptionPane.showMessageDialog(null, "Дані успішно змінено");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    public void MenuItemDataTeacher(ActionEvent event) {
        try (Statement st = DegreeProject.databaseData.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM teachers ORDER BY name")) {
            TreeSet<Teacher> teacherTreeSet = new TreeSet<>();
            while (rs.next()) {
                teacherTreeSet.add(new Teacher(rs.getInt("k"), rs.getString("name"),
                        Preference.parsePreference(rs.getString("preferences"))));}
            Teacher[] inputData = new Teacher[teacherTreeSet.size()];
            int count = 0;
            for (Teacher teacher : teacherTreeSet) inputData[count++] = teacher;
            StudyData[] outputData = DataModifyDialog.getInstance(inputData, new DataModifyInterface() {
                public StudyData add() {return TeacherDialogModify.getModify();}
                public StudyData edit(StudyData t) {return TeacherDialogModify.getModify((Teacher) t);}
                public boolean remove(StudyData t) {return true;}
                public void exit(StudyData[] t) {}
            }, "Налаштування викладачів");

            boolean b = true;
            if (outputData.length == inputData.length)
                for (int i = 0; i < outputData.length; i++) {
                    Teacher in = inputData[i];
                    Teacher out = (Teacher) outputData[i];
                    if (!in.equals(out)) {
                        b = false;
                        break;
                    }
                }
            else b = false;
            if (b) return;
            for (Teacher in : inputData) {
                boolean bool = true;
                for (StudyData out : outputData) {
                    if (in.getKey() == out.getKey() || !out.keyExist()) {
                        bool = false;
                        break;}
                    //Обробка випадку з Preference
                    if (out instanceof Teacher) {
                        boolean equals = ((Teacher) (out)).getPreference().equals(in.getPreference());
                        if (out.getKey() == in.getKey() && out.keyExist() && in.keyExist() && !equals) {
                            bool = false;
                            break;
                        }
                    }
                }
                if (bool) st.execute("DELETE FROM teachers WHERE k LIKE '" + in.getKey() + "'");
            }

            for (StudyData item : outputData) {
                if (item.keyExist())
                    st.execute("INSERT INTO teachers(name, preferences, k) VALUE ('" + item.getName() + "', '"
                            + ((Teacher) item).getPreference().getData() + "', " + item.getKey() +
                            ") ON DUPLICATE KEY UPDATE name = '" + item.getName() + "', preferences = '" + ((Teacher) item).getPreference().getData() + "';");
                else
                    st.execute("INSERT INTO teachers(name, preferences) VALUE ('"
                            + item.getName() + "', '"
                            + ((Teacher) item).getPreference().getData() + "');");
            }
            DegreeProject.mainForm.setStatusBar("Дані успішно збережено до бази даних");
            JOptionPane.showMessageDialog(null, "Дані успішно змінено");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    public void MenuItemDataAuditory(ActionEvent event) {
        try (Statement st = DegreeProject.databaseData.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM auditorys ORDER BY name")) {
            TreeSet<Auditory> auditoryTreeSet = new TreeSet<>();
            while (rs.next()) {
                auditoryTreeSet.add(new Auditory(rs.getInt("k"), rs.getString("name")));
            }
            Auditory[] inputData = new Auditory[auditoryTreeSet.size()];
            int count = 0;
            for (Auditory auditory : auditoryTreeSet) inputData[count++] = auditory;
            StudyData[] outputData = DataModifyDialog.getInstance(inputData, new DataModifyInterface() {
                public StudyData add() {
                    return AuditoryDialogModify.getModify();
                }
                public StudyData edit(StudyData t) {
                    return AuditoryDialogModify.getModify((Auditory) t);
                }
                public boolean remove(StudyData t) {
                    return true;
                }
                public void exit(StudyData[] t) {}
            }, "Налаштування аудиторій");

            boolean b = true;
            if (outputData.length == inputData.length)
                for (int i = 0; i < outputData.length; i++) {
                    Auditory in = inputData[i];
                    Auditory out = (Auditory) outputData[i];
                    if (!in.equals(out)) {
                        b = false;
                        break;
                    }
                }
            else b = false;
            if (b) return;
            for (Auditory in : inputData) {
                boolean bool = true;
                for (StudyData out : outputData) {
                    if (in.getKey() == out.getKey() || !out.keyExist()) {
                        bool = false;
                        break;
                    }
                }
                if (bool) st.execute("DELETE FROM auditorys WHERE k LIKE '" + in.getKey() + "'");
            }

            for (StudyData item : outputData) {
                if (item.keyExist())
                    st.execute("INSERT INTO auditorys(name, k) VALUE ('" + item.getName() + "', " + item.getKey() + ")" +
                            "ON DUPLICATE KEY UPDATE name = '" + item.getName() + "'");
                else
                    st.execute("INSERT INTO auditorys(name) VALUE ('" + item.getName() + "');");
            }
            DegreeProject.mainForm.setStatusBar("Дані успішно збережено до бази даних");
            JOptionPane.showMessageDialog(null, "Дані успішно змінено");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    public void MenuItemCreateLessons(ActionEvent event) {
        this.mainForm.addEmptyLessonsSchedule();
    }

    public void MenuItemRemoveSchedule(ActionEvent event) {
        ScheduleRemoveDialog form = new ScheduleRemoveDialog(DegreeProject.databaseData.getConnection());
        form.setVisible(true);
    }

    public void MenuItemViewSchedule(ActionEvent event) {
        ScheduleChoiceDialog form = new ScheduleChoiceDialog(DegreeProject.databaseData.getConnection());
        form.setVisible(true);
    }

    public void MenuItemCreateSchedule(ActionEvent event) {
        this.mainForm.addEmptyStudySchedule();
    }

    public void MenuItemExit(ActionEvent event) {
        int result = JOptionPane.showConfirmDialog(null, "Увага! Всі не збережені зміни будуть видалені\n\rПродовжити?", "Вихід", JOptionPane.YES_NO_CANCEL_OPTION);
        if (result == YES_OPTION) System.exit(0);
    }

    public void MenuItemReconnect(ActionEvent event) {
        (new ConnectionForm()).setVisible(true);
        mainForm.dispose();
    }
}
