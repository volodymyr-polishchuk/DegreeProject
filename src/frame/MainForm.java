package frame;

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
            JButton button = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/cloud-computing.png"))));
            button.addActionListener(menuBar::MenuItemSetting);
            jToolBar.add(button);
            jToolBar.add(new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/settingsIcon.png")))));
            jToolBar.add(new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/exitIcon.png")))));
            jToolBar.add(new JToolBar.Separator());
            jToolBar.add(new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/calendarIcon.png")))));
            jToolBar.add(new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/calendarSearchIcon.png")))));
            jToolBar.add(new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/calendarExportIcon.png")))));
            jToolBar.add(new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/calendarRemoveIcon.png")))));
            jToolBar.add(new JToolBar.Separator());
            jToolBar.add(new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resource/lessonIcon.png")))));
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
