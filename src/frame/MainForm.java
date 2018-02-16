package frame;

import app.DegreeProject;
import app.data.Auditory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

/**
 * Created by Vladimir on 09/01/18.
 **/
public class MainForm extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JLabel statusBar;
    private JToolBar toolBar;

    public MainForm() {
        setJMenuBar(new myMenuBar(this));

        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 600);
        setExtendedState(MAXIMIZED_BOTH);
        setTitle("Система автоматизації складання розкладу занять");
        InitJToolBar(toolBar);
        setVisible(true);
    }

    public void removeSelectedTab() {
        tabbedPane.remove(tabbedPane.getSelectedIndex());
    }

    public void addTab(JPanel jPanel) {
        tabbedPane.add(jPanel);
    }

    public void addTab(JPanel jPanel, String title) {
        tabbedPane.add(jPanel);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(new JLabel(title + " "), BorderLayout.CENTER);
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/closeIcon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JButton button = new JButton(icon);
        button.setOpaque(false);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        try {
            button.setPressedIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/closeIconPressed.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        button.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    null,
                    "Всі не збережі зміни будуть втрачені! \n\rПродовжити?",
                    "Увага",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
            );
            switch (result) {
                case JOptionPane.YES_OPTION: break;
                case JOptionPane.NO_OPTION: return;
            }
            tabbedPane.remove(tabbedPane.indexOfComponent(jPanel));
        });
        panel.add(button, BorderLayout.LINE_END);
        tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(jPanel), panel);
        tabbedPane.setSelectedComponent(jPanel);
    }

    public void setStatusBar(String s) {
        statusBar.setText(s);
    }

    private void InitJToolBar(JToolBar jToolBar) {
        try {
            jToolBar.add(new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/cloud-computing.png")))));
            jToolBar.add(new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/cloud-computing.png")))));
            jToolBar.add(new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/cloud-computing.png")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void StudyProcessAdd() {
        SchedulePanel panel = new SchedulePanel("Графік навчального процесу");
        addTab(panel, panel.getName());
        panel.showSetting();
    }

    public void LessonsProcessAdd() {
        addTab(new LessonsPanel("Розклад занять"), "Розклад занять");
    }

    private class myMenuBar extends JMenuBar {
        private MainForm mainForm;
        myMenuBar(MainForm mainForm) {
            this.mainForm = mainForm;
            //Створення головного меню програми
//          Створення пункту меню ПРОГРАМА
            JMenu programMenu = new JMenu("Програма");
            programMenu.add(new JMenuItem("Виконати підключення до іншого сервера")).addActionListener(this::MenuItemReconnect);
            programMenu.add(new JMenuItem("Налаштування"));
            programMenu.add(new JPopupMenu.Separator());
            programMenu.add(new JMenuItem("Вихід")).addActionListener(this::MenuItemExit);
            add(programMenu);

//          Створення меню Навчальний графік
            JMenu scheduleMenu = new JMenu("Навчальний графік");
            scheduleMenu.add(new JMenuItem("Створити навчальний графік")).addActionListener(this::MenuItemCreateSchedule);
            scheduleMenu.add(new JMenuItem("Переглянути/редагувати графік")).addActionListener(this::MenuItemViewSchedule);
            scheduleMenu.add(new JMenuItem("Видалити графік")).addActionListener(this::MenuItemRemoveSchedule);
            add(scheduleMenu);

//          Створення меню Розклад занять
            JMenu lessonsMenu = new JMenu("Розклад занять");
            lessonsMenu.add(new JMenuItem("Створити розклад занять")).addActionListener(this::MenuItemCreateLessons);
            lessonsMenu.add(new JMenuItem("Переглянути/редагувати розклад"));
            lessonsMenu.add(new JMenuItem("Видалити розклад"));
            add(lessonsMenu);

//          Створення меню Дані
            JMenu dataMenu = new JMenu("Дані");
            dataMenu.add(new JMenuItem("Аудиторії")).addActionListener(this::MenuItemDataAuditory);
            dataMenu.add(new JMenuItem("Викладачі"));
            dataMenu.add(new JMenuItem("Предмети"));
            dataMenu.add(new JPopupMenu.Separator());
            dataMenu.add(new JMenuItem("Групи"));
            dataMenu.add(new JPopupMenu.Separator());
            dataMenu.add(new JMenuItem("Навчальний предмет"));
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

        private void MenuItemDataAuditory(ActionEvent event) {
            try {
                Statement st = DegreeProject.databaseData.getConnection().createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM auditorys");
                LinkedList<Auditory> auditories = new LinkedList<>();
                while (rs.next()) {
                    auditories.add(new Auditory(rs.getString("name")));
                }
                Auditory[] a = new Auditory[auditories.size()];

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                e.printStackTrace();
            }
        }

        private void MenuItemCreateLessons(ActionEvent event) {
            this.mainForm.LessonsProcessAdd();
        }

        private void MenuItemRemoveSchedule(ActionEvent event) {
            ScheduleRemoveForm form = new ScheduleRemoveForm(DegreeProject.databaseData.getConnection());
            form.setVisible(true);
        }

        private void MenuItemViewSchedule(ActionEvent event) {
            ScheduleChoiceForm form = new ScheduleChoiceForm(DegreeProject.databaseData.getConnection());
            form.setVisible(true);
        }

        private void MenuItemCreateSchedule(ActionEvent event) {
            this.mainForm.StudyProcessAdd();
        }

        private void MenuItemExit(ActionEvent event) {
            int result = JOptionPane.showConfirmDialog(null, "Зберегти зміни?", "Вихід", JOptionPane.YES_NO_CANCEL_OPTION);
            switch (result) {
                case JOptionPane.YES_OPTION:
                    //TODO Збереження даних в базу даних перед виходом із програми
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
            dispose();
        }
    }
}
