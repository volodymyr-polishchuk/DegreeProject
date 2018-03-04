package app;

import app.data.*;
import frame.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;

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
        programMenu.add(new JMenuItem("Вихід")).addActionListener(this::MenuItemExit);
        add(programMenu);

//          Створення меню Навчальний графік
        JMenu scheduleMenu = new JMenu("Навчальний графік");
        scheduleMenu.add(new JMenuItem("Створити навчальний графік")).addActionListener(this::MenuItemCreateSchedule);
        scheduleMenu.add(new JPopupMenu.Separator());
        scheduleMenu.add(new JMenuItem("Переглянути/редагувати графік")).addActionListener(this::MenuItemViewSchedule);
        scheduleMenu.add(new JMenuItem("Видалити графік")).addActionListener(this::MenuItemRemoveSchedule);
        add(scheduleMenu);

//          Створення меню Розклад занять
        JMenu lessonsMenu = new JMenu("Розклад занять");
        lessonsMenu.add(new JMenuItem("Створити розклад занять")).addActionListener(this::MenuItemCreateLessons);
        lessonsMenu.add(new JPopupMenu.Separator());
        lessonsMenu.add(new JMenuItem("Переглянути/редагувати розклад")).addActionListener(this::MenuItemViewLessons);
        lessonsMenu.add(new JMenuItem("Видалити розклад")).addActionListener(this::MenuItemRemoveLessons);
        add(lessonsMenu);

//          Створення меню Дані
        JMenu dataMenu = new JMenu("Дані");
        dataMenu.add(new JMenuItem("Аудиторії")).addActionListener(this::MenuItemDataAuditory);
        dataMenu.add(new JMenuItem("Викладачі")).addActionListener(this::MenuItemDataTeacher);
        dataMenu.add(new JMenuItem("Предмети")).addActionListener(this::MenuItemDataLesson);
        dataMenu.add(new JPopupMenu.Separator());
        dataMenu.add(new JMenuItem("Групи")).addActionListener(this::MenuItemDataGroup);
        add(dataMenu);

//          Створення меню Довідка
        JMenu helpMenu = new JMenu("Довідка");
        //TODO Реалізувати форму допомоги користувачеві
        JMenuItem MenuItemHelp = new JMenuItem("Допомога користувачеві");
        helpMenu.add(MenuItemHelp);
        //TODO Реалізувати форму перевірки оновлення та передення на сторінку GitHub
        helpMenu.add(new JMenuItem("Перевірка оновлень"));
        //TODO Реалізувати форму Про програму
        JMenuItem MenuItemAbout = new JMenuItem("Про програму");
        helpMenu.add(MenuItemAbout);
        add(helpMenu);
    }

    public void MenuItemRemoveLessons(ActionEvent event) {
        LessonsRemoveDialog removeDialog = new LessonsRemoveDialog(DegreeProject.databaseData.getConnection());
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
                @Override
                public StudyData add() {
                    return GroupDialogModify.getModify();
                }

                @Override
                public StudyData edit(StudyData t) {
                    return GroupDialogModify.getModify((Group) t);
                }

                @Override
                public boolean remove(StudyData t) {
                    return true;
                }

                @Override
                public void exit(StudyData[] t) {

                }
            });

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
                    if (in.equals(out)) {
                        bool = false;
                        break;
                    }
                }
                if (bool) st.execute("DELETE FROM groups WHERE k LIKE '" + in.getKey() + "'");
            }
            PreparedStatement ps = DegreeProject.databaseData.getConnection().prepareStatement(
                    "INSERT INTO groups(name, department, dateofcreate, k, comments) VALUE (?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE name = ?, department = ?, dateofcreate = ?, comments = ?");
            PreparedStatement psElse = DegreeProject.databaseData.getConnection().prepareStatement(
                    "INSERT INTO groups(name, department, dateofcreate, comments) VALUE (?, ?, ?, ?)");
            for (StudyData item : outputData) {
                if (item.keyExist()) {
                    ps.setString(1, item.getName());
                    ps.setInt(2, ((Group) (item)).getDepartment().getKey());
                    ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                    ps.setInt(4, item.getKey());
                    ps.setString(5, ((Group) item).getComments());
                    ps.setString(6, item.getName());
                    ps.setInt(7, ((Group) (item)).getDepartment().getKey());
                    ps.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                    ps.setString(9, ((Group) item).getComments());
                    ps.execute();
                } else {
                    psElse.setString(1, item.getName());
                    psElse.setInt(2, ((Group) item).getDepartment().getKey());
                    psElse.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                    psElse.setString(4, ((Group) item).getComments());
                    psElse.execute();
                }
            }
            ps.close();
            psElse.close();
            JOptionPane.showMessageDialog(null, "Дані успішно змінено");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    private void MenuItemDataLesson(ActionEvent event) {
        try (Statement st = DegreeProject.databaseData.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM lessons INNER JOIN auditorys ON lessons.auditory = auditorys.k")
        ) {
            TreeSet<Lesson> lessonTreeSet = new TreeSet<>();
            while (rs.next()) {
                lessonTreeSet.add(
                        new Lesson(
                                rs.getInt("k"),
                                rs.getString("name"),
                                new Auditory(rs.getInt("auditorys.k"), rs.getString("auditorys.name"))
                        )
                );
            }
            Lesson[] inputData = new Lesson[lessonTreeSet.size()];
            int count = 0;
            for (Lesson lesson : lessonTreeSet) inputData[count++] = lesson;
//                Ключі для аудиторії задаються не тільки тут, а й LessonsModify, тому при зміні структури ключа варто змінити і там
            StudyData[] outputData = DataModifyDialog.getInstance(inputData, new DataModifyInterface() {
                @Override
                public StudyData add() {
                    return LessonDialogModify.getModify();
                }

                @Override
                public StudyData edit(StudyData t) {
                    return LessonDialogModify.getModify((Lesson) t);
                }

                @Override
                public boolean remove(StudyData t) {
                    return true;
                }

                @Override
                public void exit(StudyData[] t) {

                }
            });

            boolean b = true;
            if (outputData.length == inputData.length)
                for (int i = 0; i < outputData.length; i++) {
                    Lesson in = inputData[i];
                    Lesson out = (Lesson) outputData[i];
                    if (!in.equals(out)) {
                        b = false;
                        break;
                    }
                }
            else b = false;
            if (b) return;
            for (Lesson in : inputData) {
                boolean bool = true;
                for (StudyData out : outputData) {
                    if (in.equals(out)) {
                        bool = false;
                        break;
                    }
                }
                if (bool) st.execute("DELETE FROM lessons WHERE k LIKE '" + in.getKey() + "'");
            }

            for (StudyData item : outputData) {
                if (item.keyExist())
                    st.execute("INSERT INTO lessons(name, auditory, k) VALUE ('" + item.getName() + "', '"
                            + ((Lesson) item).getAuditory().getKey() + "', " + item.getKey() + ") " +
                            "ON DUPLICATE KEY UPDATE name = '" + item.getName() + "', auditory = '" + ((Lesson) item).getAuditory().getKey() + "'");
                else
                    st.execute("INSERT INTO lessons(name, auditory) VALUE ('" + item.getName() + "', '" + ((Lesson) item).getAuditory().getKey() + "');");
            }
            JOptionPane.showMessageDialog(null, "Дані успішно змінено");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    private void MenuItemDataTeacher(ActionEvent event) {
        try (Statement st = DegreeProject.databaseData.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM teachers")) {
            TreeSet<Teacher> teacherTreeSet = new TreeSet<>();
            while (rs.next()) {
                teacherTreeSet.add(
                        new Teacher(
                                rs.getInt("k"),
                                rs.getString("name"),
                                Preference.parsePreference(rs.getString("preferences"))));
            }
            Teacher[] inputData = new Teacher[teacherTreeSet.size()];
            int count = 0;
            for (Teacher teacher : teacherTreeSet) inputData[count++] = teacher;
            StudyData[] outputData = DataModifyDialog.getInstance(inputData, new DataModifyInterface() {
                @Override
                public StudyData add() {
                    return TeacherDialogModify.getModify();
                }

                @Override
                public StudyData edit(StudyData t) {
                    return TeacherDialogModify.getModify((Teacher) t);
                }

                @Override
                public boolean remove(StudyData t) {
                    return true;
                }

                @Override
                public void exit(StudyData[] t) {

                }
            });

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
                    if (in.equals(out)) {
                        bool = false;
                        break;
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
            JOptionPane.showMessageDialog(null, "Дані успішно змінено");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    private void MenuItemDataAuditory(ActionEvent event) {
        try (Statement st = DegreeProject.databaseData.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM auditorys")) {
            TreeSet<Auditory> auditoryTreeSet = new TreeSet<>();
            while (rs.next()) {
                auditoryTreeSet.add(new Auditory(rs.getInt("k"), rs.getString("name")));
            }
            Auditory[] inputData = new Auditory[auditoryTreeSet.size()];
            int count = 0;
            for (Auditory auditory : auditoryTreeSet) inputData[count++] = auditory;
            StudyData[] outputData = DataModifyDialog.getInstance(inputData, new DataModifyInterface() {
                @Override
                public StudyData add() {
                    return AuditoryDialogModify.getModify();
                }

                @Override
                public StudyData edit(StudyData t) {
                    return AuditoryDialogModify.getModify((Auditory) t);
                }

                @Override
                public boolean remove(StudyData t) {
                    return true;
                }

                @Override
                public void exit(StudyData[] t) {

                }
            });

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
                    if (in.equals(out)) {
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
            JOptionPane.showMessageDialog(null, "Дані успішно змінено");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    private void MenuItemCreateLessons(ActionEvent event) {
        this.mainForm.addEmptyLessonsSchedule();
    }

    private void MenuItemRemoveSchedule(ActionEvent event) {
        ScheduleRemoveDialog form = new ScheduleRemoveDialog(DegreeProject.databaseData.getConnection());
        form.setVisible(true);
    }

    private void MenuItemViewSchedule(ActionEvent event) {
        ScheduleChoiceDialog form = new ScheduleChoiceDialog(DegreeProject.databaseData.getConnection());
        form.setVisible(true);
    }

    private void MenuItemCreateSchedule(ActionEvent event) {
        this.mainForm.addEmptyStudySchedule();
    }

    private void MenuItemExit(ActionEvent event) {
        int result = JOptionPane.showConfirmDialog(null, "Увага! Всі не збережені зміни будуть видалені\n\rПродовжити?", "Вихід", JOptionPane.YES_NO_CANCEL_OPTION);
        switch (result) {
            case JOptionPane.YES_OPTION:
                System.exit(0);
                break;
            case JOptionPane.NO_OPTION:
                System.exit(0);
                break;
            case JOptionPane.CANCEL_OPTION:
                break;
        }
    }

    private void MenuItemReconnect(ActionEvent event) {
        (new ConnectionForm()).setVisible(true);
        mainForm.dispose();
    }
}
