package app;

import app.data.GroupList;
import app.data.WeekList;
import app.frame.ConnectionForm;
import app.frame.IntroFrame;
import app.frame.MainForm;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * Created by Vladimir on 01/01/18.
 **/
public class DegreeProject {
    public static final int version = 1000000001;
    public static DatabaseData databaseData;
    public static WeekList WEEKLIST;
    public static GroupList GROUPLIST;
    public static MainForm mainForm;
    public static String defaultDB = "asfsc";
    public static Image mainIcon;

    public static void main(String[] args) throws IOException, SQLException {
        loadIcon(); // Завантаження ресурсів
        loadConnectionForm(); // Завантаження форми
    }

    private static void loadIcon() {
        mainIcon = new ImageLoad("/resource/icon/icon2.png").getImage();
    }

    private static class ImageLoad {
        private Image im = null;

        ImageLoad(String path) {
            InputStream inputStream = getClass().getResourceAsStream(path);
            if (inputStream != null) try {
                im = ImageIO.read(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Image getImage() {
            return im;
        }
    }

    private static void loadConnectionForm() {
        final IntroFrame introFrame = new IntroFrame();
        introFrame.setVisible(true);
        new Thread(() -> {
            while (introFrame.getImagePanel().nextStep() < introFrame.getImagePanel().getMax()) {
                try { Thread.sleep(30); } catch (InterruptedException e) {/**/}
            }}).start();
        setCustomLookAndFeel();
        try {
            ConnectionForm connectionForm = new ConnectionForm(true);
            connectionForm.setVisible(true);
            introFrame.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
            throw e;
        }
    }

    private static void setCustomLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
            UIManager.put("OptionPane.yesButtonText"   , "Так");
            UIManager.put("OptionPane.noButtonText"    , "Ні");
            UIManager.put("OptionPane.cancelButtonText", "Відміна");
            UIManager.put("OptionPane.okButtonText"    , "Готово");
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void InitialMainFrame() {
        new Thread(DegreeProject::connectionChecker).start();
        mainForm = new MainForm();
        mainForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    DegreeProject.databaseData.getConnection().close();
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        try {
            WEEKLIST = new WeekList();
            GROUPLIST = new GroupList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void connectionChecker() {
        try {
            while (true) {
                Thread.sleep(1000 * 10);
                if (!databaseData.getConnection().isValid(10)) {
                    int r = JOptionPane.showConfirmDialog(null, "З'єднання розірвано!\n"
                            + UIManager.get("OptionPane.yesButtonText") + " - зачекати, "
                            + UIManager.get("OptionPane.noButtonText") + " - вийти", "Повідомлення", JOptionPane.YES_NO_OPTION);
                    if (r == JOptionPane.NO_OPTION) {
                        DegreeProject.mainForm.getMainFormMenuBar().MenuItemReconnect(null);
                        return;
                    } else {
                        if (DegreeProject.databaseData.reconnect()) {
                            JOptionPane.showMessageDialog(null, "З'єднання відновлено", "Повідомлення", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
