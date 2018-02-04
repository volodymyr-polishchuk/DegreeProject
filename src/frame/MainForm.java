package frame;

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
        button.addActionListener(e -> {tabbedPane.remove(tabbedPane.indexOfComponent(jPanel));});
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
        addTab(new SchedulePanel("Графік навчального процесу"), "Графік навчального процесу");
    }

    public void LessonsProcessAdd() {
//        addTab(new LessonsPanel("Розклад занять"), "Розклад занять");
        addTab(new NewLessonsPanel("Розклад занять"), "Розклад занять");
    }

    private class myMenuBar extends JMenuBar {
        private MainForm mainForm;
        myMenuBar(MainForm mainForm) {
            this.mainForm = mainForm;
    // Створення головного меню програми
            JMenu m1 = new JMenu("Програма");
            m1.add(new JMenuItem("Виконати підключення до іншого сервера"));
            m1.add(new JMenuItem("Налаштування"));
            m1.add(new JPopupMenu.Separator());
            m1.add(new JMenuItem("Вихід"));
            add(m1);
            JMenu m2 = new JMenu("Навчальний графік");
            JMenuItem item = new JMenuItem("Створити навчальний графік");
            item.addActionListener(e -> this.mainForm.StudyProcessAdd());
            m2.add(item);
            m2.add(new JMenuItem("Редагувати існуючий графік"));
            m2.add(new JMenuItem("Видалити або архівувати графік"));
            add(m2);
            JMenu m3 = new JMenu("Розклад занять");
    //        m3.add(new JMenuItem("Створити розклад занять"));
            JMenuItem item1 = new JMenuItem("Створити розклад занять");
            item1.addActionListener(e -> this.mainForm.LessonsProcessAdd());
            m3.add(item1);
            m3.add(new JMenuItem("Редагувати існуючих розклад"));
            m3.add(new JMenuItem("Видалити або архівувати розклад"));
            add(m3);
            JMenu m4 = new JMenu("Дані");
            m4.add(new JMenuItem("Аудиторії"));
            m4.add(new JMenuItem("Викладачі"));
            m4.add(new JMenuItem("Предмети"));
            m4.add(new JMenuItem("Навчальний предмет"));
            add(m4);

        }
    }
}
