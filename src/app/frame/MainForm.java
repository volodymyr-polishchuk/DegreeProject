package app.frame;

import app.MainFormMenuBar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by Vladimir on 09/01/18.
 **/
public class MainForm extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JLabel statusBar;
    private JToolBar toolBar;
    private MainFormMenuBar menuBar;

    public MainForm() {
        menuBar = new MainFormMenuBar(this);
        setJMenuBar(menuBar);

        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 600);
        setExtendedState(MAXIMIZED_BOTH);
        setTitle("Система автоматизації складання розкладу занять");
        InitJToolBar(toolBar);
        setVisible(true);
        addTab(new HelloPanel2("Головне меню програми", this));
    }

    public MainFormMenuBar getMainFormMenuBar() {
        return menuBar;
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
                    "Всі не збережі зміни будуть втрачені!\n\rПродовжити?",
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
            JButton reconnectToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/cloud-computing.png"))));
            reconnectToolButton.addActionListener(menuBar::MenuItemReconnect);
            reconnectToolButton.setToolTipText("Виконати перепідключення");
            jToolBar.add(reconnectToolButton);

            JButton settingToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/settingsIcon.png"))));
            settingToolButton.addActionListener(menuBar::MenuItemSetting);
            settingToolButton.setToolTipText("Налаштування");
            jToolBar.add(settingToolButton);

            JButton exitToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/exitIcon.png"))));
            exitToolButton.addActionListener(menuBar::MenuItemExit);
            exitToolButton.setToolTipText("Вийти");
            jToolBar.add(exitToolButton);

            jToolBar.add(new JToolBar.Separator());

            JButton scheduleCreateToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/calendarIcon.png"))));
            scheduleCreateToolButton.addActionListener(menuBar::MenuItemCreateSchedule);
            scheduleCreateToolButton.setToolTipText("Створити графік навчання");
            jToolBar.add(scheduleCreateToolButton);

            JButton scheduleOpenToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/calendarSearchIcon.png"))));
            scheduleOpenToolButton.addActionListener(menuBar::MenuItemViewSchedule);
            scheduleOpenToolButton.setToolTipText("Переглянути/редагувати графік навчання");
            jToolBar.add(scheduleOpenToolButton);

            JButton scheduleRemoveToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/calendarRemoveIcon.png"))));
            scheduleRemoveToolButton.addActionListener(menuBar::MenuItemRemoveSchedule);
            scheduleRemoveToolButton.setToolTipText("Видалити графік навчання");
            jToolBar.add(scheduleRemoveToolButton);

            jToolBar.add(new JToolBar.Separator());

            JButton lessonsCreateToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/lessonIconAdd.png"))));
            lessonsCreateToolButton.addActionListener(menuBar::MenuItemCreateLessons);
            lessonsCreateToolButton.setToolTipText("Створити розклад занять");
            jToolBar.add(lessonsCreateToolButton);

            JButton lessonsViewToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/lessonIconView.png"))));
            lessonsViewToolButton.addActionListener(menuBar::MenuItemViewLessons);
            lessonsViewToolButton.setToolTipText("Переглянути/редагувати розклад занять");
            jToolBar.add(lessonsViewToolButton);

            JButton lessonsRemoveToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/lessonIconRemove.png"))));
            lessonsRemoveToolButton.addActionListener(menuBar::MenuItemRemoveLessons);
            lessonsRemoveToolButton.setToolTipText("Видалення розкладу занять");
            jToolBar.add(lessonsRemoveToolButton);

            jToolBar.add(new JToolBar.Separator());

            JButton groupsToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/users-group.png"))));
            groupsToolButton.setToolTipText("Додати групу");
            groupsToolButton.addActionListener(menuBar::MenuItemDataGroup);
            jToolBar.add(groupsToolButton);

            JButton teachersToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/teachers.png"))));
            teachersToolButton.setToolTipText("Додати викладача");
            teachersToolButton.addActionListener(menuBar::MenuItemDataTeacher);
            jToolBar.add(teachersToolButton);

            JButton lessonsToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/lesson.png"))));
            lessonsToolButton.setToolTipText("Додати предмет");
            lessonsToolButton.addActionListener(menuBar::MenuItemDataLesson);
            jToolBar.add(lessonsToolButton);

            JButton auditoryToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/auditory.png"))));
            auditoryToolButton.setToolTipText("Додати аудиторію");
            auditoryToolButton.addActionListener(menuBar::MenuItemDataAuditory);
            jToolBar.add(auditoryToolButton);

            jToolBar.add(Box.createHorizontalGlue());

            jToolBar.add(new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/info.png")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addEmptyStudySchedule() {
        SchedulePanel panel = new SchedulePanel("Графік навчального процесу");
        addTab(panel, panel.getName());
        panel.showSetting();
    }

    public void addEmptyLessonsSchedule() {
        addTab(new LessonsPanel("Розклад занять"), "Розклад занять");
    }

}
