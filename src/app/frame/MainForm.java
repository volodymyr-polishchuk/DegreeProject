package app.frame;

import app.DegreeProject;
import app.MainFormMenuBar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Created by Vladimir on 09/01/18.
 **/
public class MainForm extends JFrame {
    private JPanel contentPane;
    private JTabbedPane mainTabbedPane;
    private JLabel statusBar;
    private JToolBar toolBar;
    private MainFormMenuBar mainMenuBar;


    public MainForm() {
        mainMenuBar = new MainFormMenuBar(this);
        setJMenuBar(mainMenuBar);
        if (DegreeProject.mainIcon != null) setIconImage(DegreeProject.mainIcon);
        setMinimumSize(new Dimension(1024, 720));
        setContentPane(contentPane);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setExtendedState(MAXIMIZED_BOTH);
        setTitle("Система автоматизації складання розкладу занять");
        initJToolBar(toolBar);
        addTab(new HelloPanel("Головне меню програми", this));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });
        setVisible(true);

    }

    private void onClose() {
        int result = JOptionPane.showConfirmDialog(null, "Всі незбережені зміни будуть видалені! \nПродовжити?", "Попередження", JOptionPane.YES_NO_CANCEL_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public MainFormMenuBar getMainFormMenuBar() {
        return mainMenuBar;
    }

    public void removeSelectedTab() {
        mainTabbedPane.remove(mainTabbedPane.getSelectedIndex());
    }

    private void addTab(JPanel jPanel) {
        mainTabbedPane.add(jPanel);
    }

    void addTab(JPanel jPanel, String title) {
        mainTabbedPane.add(jPanel);
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
            mainTabbedPane.remove(mainTabbedPane.indexOfComponent(jPanel));
        });
        panel.add(button, BorderLayout.LINE_END);
        mainTabbedPane.setTabComponentAt(mainTabbedPane.indexOfComponent(jPanel), panel);
        mainTabbedPane.setSelectedComponent(jPanel);
    }

    public void setStatusBar(String s) {
        statusBar.setText(s);
    }

    private void initJToolBar(JToolBar jToolBar) {
        try {
            JButton reconnectToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/cloud-computing.png"))));
            reconnectToolButton.addActionListener(mainMenuBar::MenuItemReconnect);
            reconnectToolButton.setToolTipText("Виконати перепідключення");
            jToolBar.add(reconnectToolButton);

            JButton settingToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/settingsIcon.png"))));
            settingToolButton.addActionListener(mainMenuBar::MenuItemSetting);
            settingToolButton.setToolTipText("Налаштування");
            jToolBar.add(settingToolButton);

            JButton exitToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/exitIcon.png"))));
            exitToolButton.addActionListener(mainMenuBar::MenuItemExit);
            exitToolButton.setToolTipText("Вийти");
            jToolBar.add(exitToolButton);

            jToolBar.add(new JToolBar.Separator());

            JButton scheduleCreateToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/calendarIcon.png"))));
            scheduleCreateToolButton.addActionListener(mainMenuBar::MenuItemCreateSchedule);
            scheduleCreateToolButton.setToolTipText("Створити графік навчання");
            jToolBar.add(scheduleCreateToolButton);

            JButton scheduleOpenToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/calendarSearchIcon.png"))));
            scheduleOpenToolButton.addActionListener(mainMenuBar::MenuItemViewSchedule);
            scheduleOpenToolButton.setToolTipText("Переглянути/редагувати графік навчання");
            jToolBar.add(scheduleOpenToolButton);

            JButton scheduleRemoveToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/calendarRemoveIcon.png"))));
            scheduleRemoveToolButton.addActionListener(mainMenuBar::MenuItemRemoveSchedule);
            scheduleRemoveToolButton.setToolTipText("Видалити графік навчання");
            jToolBar.add(scheduleRemoveToolButton);

            jToolBar.add(new JToolBar.Separator());

            JButton lessonsCreateToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/lessonIconAdd.png"))));
            lessonsCreateToolButton.addActionListener(mainMenuBar::MenuItemCreateLessons);
            lessonsCreateToolButton.setToolTipText("Створити розклад занять");
            jToolBar.add(lessonsCreateToolButton);

            JButton lessonsViewToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/lessonIconView.png"))));
            lessonsViewToolButton.addActionListener(mainMenuBar::MenuItemViewLessons);
            lessonsViewToolButton.setToolTipText("Переглянути/редагувати розклад занять");
            jToolBar.add(lessonsViewToolButton);

            JButton lessonsRemoveToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/lessonIconRemove.png"))));
            lessonsRemoveToolButton.addActionListener(mainMenuBar::MenuItemRemoveLessons);
            lessonsRemoveToolButton.setToolTipText("Видалення розкладу занять");
            jToolBar.add(lessonsRemoveToolButton);

            jToolBar.add(new JToolBar.Separator());

            JButton groupsToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/users-group.png"))));
            groupsToolButton.setToolTipText("Додати групу");
            groupsToolButton.addActionListener(mainMenuBar::MenuItemDataGroup);
            jToolBar.add(groupsToolButton);

            JButton teachersToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/teachers.png"))));
            teachersToolButton.setToolTipText("Додати викладача");
            teachersToolButton.addActionListener(mainMenuBar::MenuItemDataTeacher);
            jToolBar.add(teachersToolButton);

            JButton lessonsToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/lesson.png"))));
            lessonsToolButton.setToolTipText("Додати предмет");
            lessonsToolButton.addActionListener(mainMenuBar::MenuItemDataLesson);
            jToolBar.add(lessonsToolButton);

            JButton auditoryToolButton = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/auditory.png"))));
            auditoryToolButton.setToolTipText("Додати аудиторію");
            auditoryToolButton.addActionListener(mainMenuBar::MenuItemDataAuditory);
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
        LessonsPanel panel = new LessonsPanel("Розклад занять");
        addTab(panel, "Розклад занять");
        panel.showSetting();
    }

}
