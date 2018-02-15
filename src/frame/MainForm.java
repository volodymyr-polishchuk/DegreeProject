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
            JMenu m1 = new JMenu("Програма");
            JMenuItem MenuItemReconnection = new JMenuItem("Виконати підключення до іншого сервера");
            MenuItemReconnection.addActionListener(e -> {
                (new ConnectionForm()).setVisible(true);
                dispose();
            });
            m1.add(MenuItemReconnection);
            JMenuItem MenuItemSetting = new JMenuItem("Налаштування");
            m1.add(MenuItemSetting);
            m1.add(new JPopupMenu.Separator());
            JMenuItem MenuItemExit = new JMenuItem("Вихід");
            MenuItemExit.addActionListener(e -> {
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
            });
            m1.add(MenuItemExit);
            add(m1);
//          Створення меню Навчальний графік
            JMenu m2 = new JMenu("Навчальний графік");
            JMenuItem item = new JMenuItem("Створити навчальний графік");
            item.addActionListener(e -> this.mainForm.StudyProcessAdd());
            m2.add(item);
            m2.add(new JMenuItem("Переглянути/редагувати графік"));
            m2.add(new JMenuItem("Видалити графік"));
            add(m2);
//          Створення меню Розклад занять
            JMenu m3 = new JMenu("Розклад занять");
            JMenuItem item1 = new JMenuItem("Створити розклад занять");
            item1.addActionListener(e -> this.mainForm.LessonsProcessAdd());
            m3.add(item1);
            m3.add(new JMenuItem("Переглянути/редагувати розклад"));
            m3.add(new JMenuItem("Видалити розклад"));
            add(m3);
//          Створення меню Дані
            JMenu m4 = new JMenu("Дані");
            m4.add(new JMenuItem("Аудиторії"));
            m4.add(new JMenuItem("Викладачі"));
            m4.add(new JMenuItem("Предмети"));
            m4.add(new JPopupMenu.Separator());
            m4.add(new JMenuItem("Групи"));
            m4.add(new JPopupMenu.Separator());
            m4.add(new JMenuItem("Навчальний предмет"));
            add(m4);
//          Створення меню Довідка
            JMenu m5 = new JMenu("Довідка");
            //TODO Реалізувати форму допомоги користувачеві
            JMenuItem MenuItemHelp = new JMenuItem("Допомога користувачеві");
            m5.add(MenuItemHelp);
            //TODO Реалізувати форму перевірки оновлення та передення на сторінку GitHub
            m5.add(new JMenuItem("Перевірка оновлень"));
            //TODO Реалізувати форму Про програму
            JMenuItem MenuItemAbout = new JMenuItem("Про програму");
            m5.add(MenuItemAbout);
            add(m5);
        }
    }
}
